package top.sleepnano.kkorder.service;

import org.springframework.stereotype.Service;
import top.sleepnano.kkcommon.dto.OrderBackList;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.vo.Result;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public interface OrderService extends BaseService{
    Result addOrderToRedis(List<RestOrderProduct> products, String userNo);

    Result addOrderToRedis(List<RestOrderProduct> products, String secNo, String userNo, BigDecimal prise);

    Result cancelOrder(String orderNo, String userNo);

    Result paymentSucceeded(String key,String method,String status);

    void orderCacheExpired(String toString);

    List<OrderBackList> getCacheList(String userNo) throws IOException;

    List<OrderBackList> getDatList(String userNo);
}
