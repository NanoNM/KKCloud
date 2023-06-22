package top.sleepnano.kkseckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.sleepnano.kkcommon.dto.OrderItem;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.entity.Product;
import top.sleepnano.kkcommon.service.RemoteOrderService;
import top.sleepnano.kkcommon.service.RemoteProductService;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.kkseckill.dto.SeckillRedisOrder;
import top.sleepnano.kkseckill.service.SeckillService;
import top.sleepnano.kkseckill.utils.RedisUtil;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
//    @Autowired
//    ProductMapper productMapper;
//    @Autowired
//    OrderService orderService;

    @Autowired
    RemoteProductService remoteProductService;

    @Autowired
    RemoteOrderService remoteOrderService;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public Result addSeckillToRedis(List<RestOrderProduct> productNo, Integer num, Long startTime, Long endTime, BigDecimal prise) {
        // 从商品微服务获取商品信息
        List<Product> sqlProducts = remoteProductService.selectProductByNos(productNo);

        if (sqlProducts.isEmpty() || sqlProducts.size()!=productNo.size()) {
            throw new RuntimeException("无法生成秒杀: 无法找到一个或多个相应商品,或已被下架");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        sqlProducts.forEach(product -> {
            productNo.forEach(restOrderProduct -> {
                if (Objects.equals(restOrderProduct.getProductNo(), product.getProductNo())){
                    orderItems.add(new OrderItem(product,restOrderProduct.getNum(),product.getProductPrice().multiply(BigDecimal.valueOf(restOrderProduct.getNum()))));
                }
            });

        });
        String secNo = UUID.randomUUID().toString();
        SeckillRedisOrder seckillRedisOrder = new SeckillRedisOrder(secNo,
                orderItems,num,new Date(startTime),new Date(endTime),prise);
        String key = "seckill:"+secNo;
//        设置过期时间
//        BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(key);
//        long epTime = endTime-startTime;
//        setOps.expire(epTime, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(key,seckillRedisOrder);
        // 设置秒杀到期时间
        BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(key);
        long epTime = endTime-startTime;
        setOps.expire(epTime, TimeUnit.MILLISECONDS);
        return VoBuilder.ok("秒杀商品添加成功, "+ epTime/1000/60/60+"小时后过期",secNo);

    }

    @Override
    public List<SeckillRedisOrder> listCacheSeckill() throws IOException {
        String key = "seckill:*";
        List<String> keys = redisUtil.getKey(key);
        if (keys.isEmpty()){
            return null;
        }
        List<SeckillRedisOrder> seckillRedisOrders = redisTemplate.opsForValue().multiGet(keys);
        if (Objects.isNull(seckillRedisOrders) || seckillRedisOrders.isEmpty()){
            return null;
        }

        return seckillRedisOrders;
    }

    @Override
    public Result orderSeckill(String secNo,String userNo) throws IOException {
        Integer times = 0;
        String seckillKey = "seckill:" + secNo;

        String orderKey = "order:"+userNo+", orderID:"+secNo;
        List<String> keys = redisUtil.getKey(orderKey);
        if (keys.size()>0){
            return VoBuilder.wrong("你只能秒杀这个订单"+(times+1)+"次",null);
        }

        SeckillRedisOrder seckillRedisOrder = (SeckillRedisOrder) redisTemplate.opsForValue().get(seckillKey);
        if (Objects.isNull(seckillRedisOrder)){
            return VoBuilder.error("不存在的秒杀或秒杀已结束",null);
        }
        if (System.currentTimeMillis()<seckillRedisOrder.getStartTime().getTime()){
            return VoBuilder.wrong("秒杀还未开始",null);
        }
        if (System.currentTimeMillis()>seckillRedisOrder.getEndTime().getTime()){
            log.error("秒杀已结束,但是redis未删除失效的秒杀{}",seckillRedisOrder.getSeckillOrderNo());
            throw new RuntimeException("秒杀已结束,但这是一个错误请联系管理员" + seckillRedisOrder.getSeckillOrderNo());
        }
        if (seckillRedisOrder.getNum()==0){
            redisTemplate.delete(seckillKey);
            return VoBuilder.wrong("秒杀抢完了",null);
        }

        List<RestOrderProduct> restOrderProducts = new ArrayList<>();
        seckillRedisOrder.getProducts().forEach(product->{
            restOrderProducts.add(new RestOrderProduct(product.getProduct().getProductNo(),seckillRedisOrder.getNum()));
        });

        Result result = remoteOrderService.addOrder(restOrderProducts, secNo, userNo, seckillRedisOrder.getPrise());


        seckillRedisOrder.setNum(seckillRedisOrder.getNum()-1);
        redisTemplate.opsForValue().set(seckillKey,seckillRedisOrder);

//        redisTemplate.opsForValue().set(seckillOrderKey,seckillRedisOrder);

        return result;
    }
}
