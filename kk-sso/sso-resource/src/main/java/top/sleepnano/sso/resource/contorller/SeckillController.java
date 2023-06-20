package top.sleepnano.sso.resource.contorller;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.dto.SeckillRedisOrder;
import top.sleepnano.kkcommon.service.RemoteSeckillService;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.sso.resource.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    RemoteSeckillService remoteSeckillService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:query:order')")
    public Result queryOrder(@Nullable @RequestParam("pos") String pos,
                             HttpServletRequest httpServletRequest){

        String header=httpServletRequest.getHeader("Authorization");
        String token= StringUtils.substringAfter(header,"Bearer");

        try {
            Map<String, ?> infos = (Map<String, ?>) JwtUtil.parseJWT(token).get("info");
            String userNo = infos.get("userNo").toString();
            return remoteSeckillService.listCacheSeckill();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/order")
    @PreAuthorize("hasAuthority('sys:user:order:seckill')")
    public Result orderSecKill(
            @RequestParam("secNo")String secNo,
            HttpServletRequest httpServletRequest
    ){
        String header=httpServletRequest.getHeader("Authorization");
        String token= StringUtils.substringAfter(header,"Bearer");
        try {
            Map<String, ?> infos = (Map<String, ?>) JwtUtil.parseJWT(token).get("info");
            String userNo = infos.get("userNo").toString();
            return remoteSeckillService.orderSeckill(secNo,userNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }}

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('sys:admin:add:seckill')")
    public Result addSeckill(@RequestBody List<RestOrderProduct> productNo,
                             @RequestParam("num")Integer num,
                             @RequestParam("st")Long startTime,
                             @RequestParam("et")Long endTime,
                             @RequestParam("prise")Long prise,
                             HttpServletRequest httpServletRequest){
        return remoteSeckillService.addSeckillToRedis(productNo,num,startTime,endTime, BigDecimal.valueOf(prise));
    }

}
