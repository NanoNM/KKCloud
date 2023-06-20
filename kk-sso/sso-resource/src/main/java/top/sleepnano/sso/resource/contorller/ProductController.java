package top.sleepnano.sso.resource.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.sleepnano.kkcommon.service.RemoteProductService;
import top.sleepnano.kkcommon.util.VoBuilder;
import top.sleepnano.kkcommon.vo.Result;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    RemoteProductService remoteProductService;

    @GetMapping("/list")
    Result listProduct(@RequestParam("page") Integer page) {
        return remoteProductService.getUserById(page);
    }

    @GetMapping("/test")
    Result testProduct() {
        return remoteProductService.testProduct();
    }
}
