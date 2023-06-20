package top.sleepnano.sso.auth.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String userName;

    private String userPass;

    private String userAddr;

    private String userPhone;

    private String userNo;

    private LocalDate createTime;

    private LocalDate modifyTime;
}

