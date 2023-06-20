package top.sleepnano.kkcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.sleepnano.kkcommon.entity.Product;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    Product product;
    Integer num;
    BigDecimal allPrice;
}

