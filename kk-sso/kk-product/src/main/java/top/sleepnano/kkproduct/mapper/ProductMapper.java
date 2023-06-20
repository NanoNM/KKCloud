package top.sleepnano.kkproduct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkproduct.entity.Product;


import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    @Select("Select " +
            "id," +
            "product_name," +
            "product_category," +
            "product_price," +
            "product_desc," +
            "product_no," +
            "img," +
            "product_hits," +
            "product_status," +
            "create_time," +
            "modify_time from product_list")
    List<Product> selectAllProduct();

    @Select("Select " +
            "id," +
            "product_name," +
            "product_category," +
            "product_price," +
            "product_desc," +
            "product_no," +
            "img," +
            "product_hits," +
            "product_status," +
            "create_time," +
            "modify_time from product_list limit #{page},#{pageSize}")
    List<Product> selectProductByPage(@Param("page") Integer page,@Param("pageSize") Integer pageSize);

    @Select("Select " +
            "id," +
            "product_name," +
            "product_category," +
            "product_price," +
            "product_desc," +
            "product_no," +
            "img," +
            "product_hits," +
            "product_status," +
            "create_time," +
            "modify_time from product_list where product_no = #{productNo}")
    Product selectProductByNo(@Param("productNo") String productNo);

//    @Select("Select " +
//            "id," +
//            "product_name," +
//            "product_category," +
//            "product_price," +
//            "product_desc," +
//            "product_no," +
//            "img," +
//            "product_hits," +
//            "product_status," +
//            "create_time," +
//            "modify_time from product_list where product_no IN(#{nos})")
//    List<Product> selectProductByNos(List<RestOrderProduct> nos);

    List<Product> selectProductByNosString(List<String> nos);

    List<Product> selectProductByNos(List<RestOrderProduct> nos);



}
