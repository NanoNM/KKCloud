package top.sleepnano.kkorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.dto.OrderBackList;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.kkorder.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;



    @PostMapping("/add")
    public Result addOrder(@RequestBody List<RestOrderProduct> products, @RequestParam("userNo")String userNo){
        try {
            return orderService.addOrderToRedis(products,userNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/addsec")
    public Result addOrder(@RequestBody List<RestOrderProduct> restOrderProducts,
                           @RequestParam("secNo") String secNo,
                           @RequestParam("userNo") String userNo,
                           @RequestParam("prise") BigDecimal prise){
        try {
            return orderService.addOrderToRedis(restOrderProducts, secNo, userNo, prise);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/cancel")
    public Result cancelOrder(@RequestParam("orderNo")String orderNo,
                              @RequestParam("userNo")String userNo){
        try {
            return orderService.cancelOrder(orderNo,userNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/list")
    public Result queryOrder(@Nullable @RequestParam("pos") String pos,
                             @Nullable @RequestParam("page") Integer page,
                             @RequestParam("userNo")String userNo){
        try {
            if ("cache".equals(pos)){
                List<OrderBackList> cacheList = orderService.getCacheList(userNo);
                if (Objects.isNull(cacheList)){
                    return VoBuilder.ok("缓存订单为空",null);
                }
                return VoBuilder.ok("缓存订单查询成功",cacheList);
            } else if("dat".equals(pos)){
                List<OrderBackList> datList = orderService.getDatList(userNo);
                if (Objects.isNull(datList)){
                    return VoBuilder.ok("数据库订单为空",null);
                }
                return VoBuilder.ok("数据库订单查询成功",datList);
            } else {
                List<OrderBackList> cacheList = orderService.getCacheList(userNo);
                List<OrderBackList> datList = orderService.getDatList(userNo);
                Map<String,List> orderMap = new HashMap<>();
                orderMap.put("cache",cacheList);
                orderMap.put("data",datList);

                return VoBuilder.ok("订单查询成功",orderMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/paySucceeded")
    Result paymentSucceeded(@RequestParam("key")String key,
                            @RequestParam("payMethodCode")String payMethodCode,
                            @RequestParam("payMethod")String payMethod,
                            @RequestParam("trade_status1")String trade_status){

        return orderService.paymentSucceeded(key, payMethod,payMethodCode, trade_status);
    }
}
