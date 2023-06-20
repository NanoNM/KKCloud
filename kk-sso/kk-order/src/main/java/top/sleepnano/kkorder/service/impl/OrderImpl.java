package top.sleepnano.kkorder.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sleepnano.kkorder.entity.SelectOrder;
import top.sleepnano.kkorder.entity.SysOrder;
import top.sleepnano.kkorder.entity.UserOrder;
import top.sleepnano.kkcommon.dto.OrderBackList;
import top.sleepnano.kkcommon.dto.OrderItem;
import top.sleepnano.kkcommon.dto.OrderRedis;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.entity.Product;
import top.sleepnano.kkcommon.service.RemoteProductService;
import top.sleepnano.kkcommon.util.RandomUtil;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.kkorder.entity.OrderProduct;
import top.sleepnano.kkorder.mapper.OrderMapper;
import top.sleepnano.kkorder.service.OrderService;
import top.sleepnano.kkorder.utils.RedisUtil;


import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderImpl implements OrderService {
    @Value("${kk.redis.order.expirationTime}")
    Integer expirationTime;
//    @Autowired
    @Resource
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RemoteProductService remoteProductService;

//    @Autowired
//    ProductMapper productMapper;
//    @Autowired
//    OrderMapper orderMapper;


    /**
     * 生成临时订单
     * @param products 商品列表
     * @param userNo 用户no
     * @return 返回Vo类
     */
    @Override
    public Result addOrderToRedis(List<RestOrderProduct> products, String userNo) {
        // 从商品微服务取出具体商品
        List<Product> sqlProducts = remoteProductService.selectProductByNos(products);
        // 如果没有相关商品
        if (sqlProducts.isEmpty() || sqlProducts.size()!=products.size()) {
            throw new RuntimeException("无法生成订单: 无法找到一个或多个相应商品,或已被下架");
        }
        // 生成订单对象
        List<OrderItem> orderItems = new ArrayList<>();
        sqlProducts.forEach(it->{
            Integer num = products.stream().filter(
                    i->i.getProductNo().equals(it.getProductNo())).collect(Collectors.toList()).get(0).getNum();
            orderItems.add(new OrderItem(
                    it,num, it.getProductPrice().multiply(BigDecimal.valueOf(num)))
            );
        });
        // 计算总金额
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            bigDecimal = bigDecimal.add(orderItem.getAllPrice());
        }
        // 生成随机订单编号
        UUID uuid= RandomUtil.genUUID();
        OrderRedis orderRedis = new OrderRedis(userNo,uuid,orderItems,bigDecimal,0,new Date(),0);
        String key = "order:"+userNo+", orderID:"+uuid;
        String keyTmp = "TMP_" + "order:"+userNo+", orderID:"+uuid;
        redisTemplate.opsForValue().set(key, orderRedis);
        redisTemplate.opsForValue().set(keyTmp, orderRedis);
        // 设置订单到期时间
        BoundSetOperations<String, Object> setOps = redisTemplate.boundSetOps(key);
        setOps.expire(expirationTime, TimeUnit.SECONDS);

        return VoBuilder.ok("订单创建成功, "+ expirationTime/60 +"分钟后过期 , 请尽快支付",uuid);
    }

    @Override
    public Result addOrderToRedis(List<RestOrderProduct> products, String secNo, String userNo, BigDecimal prise) {
        // 从商品微服务取出具体商品
        List<Product> sqlProducts = remoteProductService.selectProductByNos(products);
        // 如果没有相关商品
        if (sqlProducts.isEmpty() || sqlProducts.size()!=products.size()) {
            throw new RuntimeException("无法生成订单: 无法找到一个或多个相应商品,或已被下架");
        }
        // 生成订单对象
        List<OrderItem> orderItems = new ArrayList<>();
        sqlProducts.forEach(it->{
            Integer num = products.stream().filter(
                    i->i.getProductNo().equals(it.getProductNo())).collect(Collectors.toList()).get(0).getNum();
            orderItems.add(new OrderItem(
                    it,num, it.getProductPrice().multiply(BigDecimal.valueOf(num)))
            );
        });

        // 订单No使用秒杀No
        UUID uuid= UUID.fromString(secNo);
        OrderRedis orderRedis = new OrderRedis(userNo,uuid,orderItems,prise,0,new Date(),0);
        String key = "order:"+userNo+", orderID:"+uuid;
        String keyTmp = "TMP_" + "order:"+userNo+", orderID:"+uuid;
        redisTemplate.opsForValue().set(key, orderRedis);
        redisTemplate.opsForValue().set(keyTmp, orderRedis);
        BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(key);
        setOps.expire(expirationTime, TimeUnit.SECONDS);
        return VoBuilder.ok("订单创建成功, "+ expirationTime/60 +"分钟后过期 , 请尽快支付",uuid);
    }

    @Override
    public Result cancelOrder(String orderNo, String userNo) {
        // List<String> key = redisUtil.getKey("*" + userNo + "*");
        // 删除还存在Redis中的数据
        Boolean delete = redisTemplate.delete("order:" + userNo + ", orderID:" + orderNo);
        Boolean deleteTmp = redisTemplate.delete("TMP_" + "order:"+userNo+", orderID:" + orderNo);
        if (Boolean.FALSE.equals(delete) || Boolean.FALSE.equals(deleteTmp)){
            throw new RuntimeException("无法取消订单,订单已失效或已被支付");
        }
        return VoBuilder.ok("订单取消成功",null);
    }

    @Override
    public Result paymentSucceeded(String key, String method, String status) {
        return null;
    }

    @Override
    public void orderCacheExpired(String key) {
        OrderRedis orderRedis = (OrderRedis) redisTemplate.opsForValue().get("TMP_"+key);
        if (Objects.isNull(orderRedis)){
            log.error("严重异常,订单缓存到期,但是未查询到改订单信息 {}",key);
            throw new RuntimeException("严重异常,订单缓存到期,但是未查询到改订单信息:" + "TMP_"+key);
        }
        // 过期订单入库
        SysOrder sysOrder = new SysOrder(null,
                orderRedis.getOrderId().toString(),
                0,
                "超时",
                "TRADE_CLOSED","timeout", null, null,null,null,
                orderRedis.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        orderReceiptTransaction(sysOrder,key,orderRedis,"orderCacheExpired");
    }

    @Override
    public List<OrderBackList> getCacheList(String userNo) throws IOException {
        String key = "order:"+userNo+"*";
        List<String> keys = redisUtil.getKey(key);
        if (keys.isEmpty()){
            return null;
        }
        List<OrderRedis> cacheList = redisTemplate.opsForValue().multiGet(keys);
        if (Objects.isNull(cacheList) || cacheList.isEmpty()){
            return null;
        }
        List<OrderBackList> orderBackLists = new ArrayList<>();
        cacheList.forEach(orderRedis -> {
            Integer expireTime = Objects.requireNonNull(redisTemplate.opsForValue().getOperations().getExpire(
                    "order:"+orderRedis.getUserNo()+", orderID:"+orderRedis.getOrderId()
            )).intValue();

            orderBackLists.add(new OrderBackList(
                    orderRedis.getOrderId().toString(),"TRADE_CREATE",
                    orderRedis.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    null,
                    null,
                    null,
                    expireTime,
                    orderRedis.getAllPrice(),
                    orderRedis.getOrderItems()
            ));
        });

        return orderBackLists;
    }

    @Override
    public List<OrderBackList> getDatList(String userNo) {
        // 根据用户No取出订单
        List<String> userOrderList = orderMapper.getUserOrderList(userNo);
        List<OrderBackList> orderBackLists = new ArrayList<>();
        AtomicReference<OrderBackList> orderBackList = new AtomicReference<>();
        // 循环所有订单
        userOrderList.forEach(userOrderNo -> {
            // 获取订单对应商品
            List<SelectOrder> datOrderList = orderMapper.getDatOrderList(userOrderNo);
            List<OrderItem> orderItems = new ArrayList<>();
            datOrderList.forEach(selectOrder -> {
                // 获取订单详细信息
                SysOrder sysOrder = orderMapper.selectSysOrderByOrderNo(selectOrder.getOrderNo());
                orderBackList.set(new OrderBackList(
                        userOrderNo,
                        sysOrder.getStatus(),
                        sysOrder.getGenTime(),
                        sysOrder.getCreateTime(),
                        sysOrder.getSentTime(),
                        sysOrder.getCompletionTime(),
                        null,
                        null,
                        null
                ));
                // 存入订单中的商品信息
                orderItems.add(new OrderItem(
                        selectOrder.getProduct(),selectOrder.getOrderQuantity(),
                        selectOrder.getProduct().getProductPrice().multiply(BigDecimal.valueOf(selectOrder.getOrderQuantity()))
                ));
            });
            // 封装返回数据
            orderBackList.get().setOrderItems(orderItems);
            orderBackLists.add(orderBackList.get());
        });
        return orderBackLists;
    }

    @Transactional
    public void orderReceiptTransaction(SysOrder sysOrder,String key,OrderRedis orderRedis,String caller){
        // 订单入库
        Integer insertPaidOrder = orderMapper.insertPaidOrder(sysOrder);
        if (insertPaidOrder==0){
            log.error("严重异常,订单入库失效,但是付款成功 {}",key);
            throw new RuntimeException("严重异常,订单入库失效,来自:" + key + ", 调用者:"+caller);
        }
        // 订单绑定用户
        UserOrder userOrder = new UserOrder(orderRedis.getUserNo(),orderRedis.getOrderId().toString());
        Integer integer = orderMapper.insertOrderUser(userOrder);
        if (integer==0){
            log.error("严重异常,订单绑定用户失败,但是付款成功 {}",key);
            throw new RuntimeException("严重异常,订单绑定用户失败,来自:" + key + ", 调用者:"+caller);
        }
        // 订单绑定商品
        List<OrderProduct> orderProducts = new ArrayList<>();
        orderRedis.getOrderItems().forEach(orderItem -> {
            OrderProduct orderProduct = new OrderProduct(orderRedis.getOrderId().toString(), orderItem.getProduct().getProductNo(), orderItem.getNum());
            orderProducts.add(orderProduct);
        });
        Integer integer1 = orderMapper.insertOrderProduct(orderProducts);
        if (integer1!=orderProducts.size()){
            log.error("严重异常,订单绑定商品失败,但是付款成功 {}",key);
            throw new RuntimeException("严重异常,订单绑定商品失败,来自:" + key + ", 调用者:"+caller);
        }
        // 删除缓存中的订单
        log.info(caller +"删除key :{}",key);
        Boolean delete = redisTemplate.delete(key);
        log.info(caller + "删除key :{}","TMP_"+key);
        redisTemplate.delete("TMP_" + key);
    }
}
