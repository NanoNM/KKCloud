package top.sleepnano.kkorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_order")
@AllArgsConstructor
@NoArgsConstructor
public class SysOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orderNo;

    private Integer isPaid;

    private String paymentMethod;

    private String paymentCode;

    private String status;

    private LocalDate createTime;

    private LocalDate modifyTime;

    private LocalDate sentTime;

    private LocalDate completionTime;

    private LocalDate genTime;


}
