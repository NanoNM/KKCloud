package top.sleepnano.kkcommon.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.vo.Result;

import java.math.BigDecimal;
import java.util.List;

//@Service
@FeignClient("kk-order")
@RequestMapping("/order")
public interface RemoteOrderService {
    @RequestMapping("/add")
    Result addOrder(@RequestBody List<RestOrderProduct> products, @RequestParam("userNo") String userNo);

    @RequestMapping("/addsec")
    Result addOrder(@RequestBody List<RestOrderProduct> restOrderProducts,
                  @RequestParam("secNo") String secNo,
                  @RequestParam("userNo") String userNo,
                  @RequestParam("prise") BigDecimal prise);

//    @RequestMapping("/add2redis")
//    Result addOrderToRedis(@RequestBody List<RestOrderProduct> restOrderProducts,
//                           @RequestParam("secNo") String secNo,
//                           @RequestParam("userNo") String userNo,
//                           @RequestParam("prise") BigDecimal prise);
//    @RequestMapping("/add")
//    Result addOrder(@RequestBody List<RestOrderProduct> products, @RequestParam("userNo") String userNo);

    @RequestMapping("/cancel")
    Result cancelOrder(@RequestParam("orderNo") String orderNo, @RequestParam("userNo") String userNo);


    @RequestMapping("/test")
    Result testProduct();

    @RequestMapping("/list")
    Result queryOrder(@Nullable @RequestParam("pos") String pos,
                             @Nullable @RequestParam("page") Integer page,
                             @RequestParam("userNo")String userNo);


    @RequestMapping("/paySucceeded")
    Result paymentSucceeded(@RequestParam("key")String key,
                            @RequestParam("payMethodCode")String payMethodCode,
                            @RequestParam("payMethod")String payMethod,
                            @RequestParam("trade_status1")String trade_status);
}

//@FeignClient("user-service")
//public interface UserClient {
//
//
//}

