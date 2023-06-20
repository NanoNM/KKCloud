package top.sleepnano.kkseckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.kkseckill.dto.SeckillRedisOrder;
import top.sleepnano.kkseckill.service.SeckillService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController()
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    SeckillService seckillService;

    @GetMapping("/listCacheSeckill")
    public Result listSecKill(){
        List<SeckillRedisOrder> seckillRedisOrders = null;
        try {
            seckillRedisOrders = seckillService.listCacheSeckill();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return VoBuilder.ok("查询成功",seckillRedisOrders);
    }

    @GetMapping("/order")
    public Result orderSecKill(
            @RequestParam("secNo")String secNo,
            @RequestParam("userNo")String userNo
    ){
        try {
            return seckillService.orderSeckill(secNo,userNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }}

    @PostMapping("/add")
    public Result addSeckillToRedis(
            @RequestBody List<RestOrderProduct> productNo,
            @RequestParam("num")Integer num,
            @RequestParam("st")Long startTime,
            @RequestParam("et")Long endTime,
            @RequestParam("prise") BigDecimal prise
    ){
        return seckillService.addSeckillToRedis(productNo,num,startTime,endTime, prise);
    }
}
