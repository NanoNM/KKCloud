package top.sleepnano.kkorder.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sleepnano.kkcommon.entity.Product;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SelectOrder {
    private String orderNo;
    private Integer orderQuantity;
    private Product product;

    @Override
    public String toString() {
        return "SelectOrder{" +
                "orderNo='" + orderNo + '\'' +
                ", orderQuantity=" + orderQuantity +
                ", product=" + product +
                '}';
    }
}
