package top.sleepnano.kkcommon.service;


import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.vo.Result;

import java.util.Map;

//@Service
@FeignClient("kk-payment")
@RequestMapping("/payment/aliPay")
public interface RemoteAliPayService {
    /**
     * 前往支付宝进行支付
     */
    @RequestMapping("/goAlipay")
    Result goAlipay(@RequestParam("merchantOrderId") String merchantOrderId,
                    @RequestParam("userNo") String userNo);

    /**
     * 支付成功后的支付宝异步通知
     */
    @RequestMapping(value = "/aliNotify")
    String alipay(@RequestBody Map<String, String> params);

    /***
     * 支付结束后转回到本地服务
     * @param params 参数
     * @return Vo
     */
    @RequestMapping("/alipayResult")
    Result alipayResult(@RequestParam("params") Map<String, String> params);
}

//@FeignClient("user-service")
//public interface UserClient {
//
//
//}

