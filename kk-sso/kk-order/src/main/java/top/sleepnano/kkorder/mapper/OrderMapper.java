package top.sleepnano.kkorder.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import top.sleepnano.kkorder.entity.SelectOrder;
import top.sleepnano.kkorder.entity.SysOrder;
import top.sleepnano.kkorder.entity.UserOrder;
import top.sleepnano.kkorder.entity.OrderProduct;


import java.util.List;

@Mapper
public interface OrderMapper {
    @DS("master")
    Integer insertPaidOrder(SysOrder sysOrder);
    @DS("master")
    Integer insertOrderUser(UserOrder userOrder);
    @DS("master")
    Integer insertOrderProduct(List<OrderProduct> orderProducts);
    List<SelectOrder> getDatOrderList(String orderNo);

    SysOrder selectSysOrderByOrderNo(String orderNo);

    List<String> getUserOrderList(String userNo);
}
