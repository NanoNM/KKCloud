package top.sleepnano.kkcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRedis {
    String userNo;
    UUID orderId;
    List<OrderItem> orderItems;
    BigDecimal allPrice;
    Integer isPayed;
    Date createDate;
    Integer delMarked;
}
