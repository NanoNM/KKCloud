package top.sleepnano.kkcommon.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.sleepnano.kkcommon.dto.RestOrderProduct;
import top.sleepnano.kkcommon.entity.Product;
import top.sleepnano.kkcommon.vo.Result;

import java.util.List;

@Service
@FeignClient("kk-product")
public interface RemoteProductService {
    @RequestMapping("/product/list")
    Result getUserById(@RequestParam("page") int page);
    @RequestMapping("/product/test")
    Result testProduct();
    @RequestMapping("/product/selectProductByNos")
    List<Product> selectProductByNos(@RequestBody List<RestOrderProduct> products);
}

//@FeignClient("user-service")
//public interface UserClient {
//
//
//}

