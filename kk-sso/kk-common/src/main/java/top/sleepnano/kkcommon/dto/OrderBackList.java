package top.sleepnano.kkcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBackList {

    private String orderNo;
    private String orderStatus;
    private LocalDate createTime;
    private LocalDate paymentTime;
    private LocalDate sendOutGoodsTime;
    private LocalDate tradeCloseTime;
    private Integer TTL;
    private BigDecimal allPrise;
    private List<OrderItem> orderItems;

   }
