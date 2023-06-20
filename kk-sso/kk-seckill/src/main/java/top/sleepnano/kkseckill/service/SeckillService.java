package top.sleepnano.kkseckill.service;

import org.springframework.stereotype.Service;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.kkseckill.dto.SeckillRedisOrder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public interface SeckillService{

    /***
     * 创建一个秒杀商品
     * @param productNo 商品们
     * @param num 秒杀总数
     * @param startTime 开始时间戳
     * @param endTime 关闭时间戳
     * @param prise 该秒杀价格
     * @return Vo
     */
    Result addSeckillToRedis(List<RestOrderProduct> productNo, Integer num, Long startTime, Long endTime, BigDecimal prise);

    /***
     * 列出活跃的秒杀
     * @return redis中的秒杀信息
     * @throws IOException 异常
     */
    List<SeckillRedisOrder> listCacheSeckill() throws IOException;

    /***
     * 下单秒杀
     * @param secNo 秒杀no
     * @param userNo 下单用户
     * @return Vo
     * @throws IOException 异常
     */
    Result orderSeckill(String secNo, String userNo) throws IOException;
}
