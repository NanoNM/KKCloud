package top.sleepnano.kkcommon.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.vo.Result;

import java.math.BigDecimal;
import java.util.List;

@Service
@FeignClient("kk-seckill")
public interface RemoteSeckillService {

    @RequestMapping("/seckill/listCacheSeckill")
    Result listCacheSeckill();

    @RequestMapping("/seckill/order")
    Result orderSeckill(@RequestParam("secNo") String secNo, @RequestParam("userNo")String userNo);

    @RequestMapping("/seckill/add")
    Result addSeckillToRedis(
            @RequestBody List<RestOrderProduct> productNo,
            @RequestParam("num")Integer num,
            @RequestParam("st")Long startTime,
            @RequestParam("et")Long endTime,
            @RequestParam("prise") BigDecimal prise
    );
}
