package top.sleepnano.kkproduct.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;
import top.sleepnano.kkproduct.entity.Product;
import top.sleepnano.kkproduct.mapper.ProductMapper;
import top.sleepnano.kkproduct.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductMapper productMapper;

    /**
     *
     * @param page 页码
     * @return VO数据
     */
    @GetMapping("/list")
    Result listProduct(@RequestParam("page") Integer page){
        return productService.getProduct(page,10);
    }

    @GetMapping("/test")
    Result testProduct(){
        return VoBuilder.ok("ok",null);
    }

    @RequestMapping("/selectProductByNos")
    List<Product> selectProductByNos(@RequestBody List<RestOrderProduct> products){
        return productMapper.selectProductByNos(products);
    }

}
