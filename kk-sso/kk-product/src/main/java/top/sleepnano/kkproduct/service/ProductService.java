package top.sleepnano.kkproduct.service;

import org.springframework.stereotype.Service;
import top.sleepnano.kkcommon.vo.Result;

@Service
public interface ProductService {
    Result getProduct(Integer num,Integer pageSize);
}
