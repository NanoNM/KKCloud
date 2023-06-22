package top.sleepnano.sso.resource.contorller;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.service.RemoteAliPayService;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.sso.resource.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/payment/aliPay")
public class AliPayController {
    @Autowired
    RemoteAliPayService remoteAliPayService;
    /**
     * 前往支付宝进行支付
     */
    @GetMapping(value="/goAlipay")
    public Result goAlipay(@RequestParam("merchantOrderId") String merchantOrderId,
                           HttpServletRequest httpServletRequest){

        String header=httpServletRequest.getHeader("Authorization");
        String token= StringUtils.substringAfter(header,"Bearer");
        try {
            Map<String, ?> infos = (Map<String, ?>) JwtUtil.parseJWT(token).get("info");

            return remoteAliPayService.goAlipay(merchantOrderId, infos.get("userNo").toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    /**
     * 支付成功后的支付宝异步通知
     */
    @PostMapping("/aliNotify")
    public String alipay(HttpServletRequest request){
        Map<String, String> params = getParams(request);

        return remoteAliPayService.alipay(params);
    }

    @GetMapping(value="/alipayResult")
    public Result alipayResult(HttpServletRequest request){
        Map<String, String> params = getParams(request);

        return remoteAliPayService.alipayResult(params);
    }

    private static Map<String, String> getParams(HttpServletRequest request) {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //       valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }
}
