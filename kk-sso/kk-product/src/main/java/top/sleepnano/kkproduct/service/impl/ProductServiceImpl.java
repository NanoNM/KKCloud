package top.sleepnano.kkproduct.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.kkproduct.entity.Product;
import top.sleepnano.kkproduct.mapper.ProductMapper;
import top.sleepnano.kkproduct.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private Result result;
//    @Value("${kk.redis.cart.limit}")
//    Integer cartLimit;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result getProduct(Integer num,Integer pageSize) {

        if (num<=0){
            return VoBuilder.error("无法从"+num+"页开始查询", null);
        }
        List<Product> products = null;
        System.out.println("num = " + num);
        products = productMapper.selectProductByPage((num - 1) * pageSize, pageSize);
        if (products.isEmpty()){
            result = VoBuilder.wrong("查询失败, 数据为空",products);
        }else {
            result = VoBuilder.ok("查询成功",products);
        }
        return result;
    }
}
