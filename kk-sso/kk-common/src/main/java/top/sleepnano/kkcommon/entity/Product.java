package top.sleepnano.kkcommon.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2023-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value="ProductList对象", description="")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String productName;

    private Integer productCategory;

    private BigDecimal productPrice;

    private String productDesc;

    private String productNo;

    private String img;

    private String productHits;

    private Integer productStatus;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


}
