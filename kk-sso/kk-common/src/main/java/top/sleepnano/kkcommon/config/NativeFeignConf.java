package top.sleepnano.kkcommon.config;

import com.alibaba.nacos.common.utils.StringUtils;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Configuration
public class NativeFeignConf {
    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 处理POST请求的时候转换成了GET的bug
                if (HttpMethod.POST.name().equals(template.method()) && template.requestBody().length() == 0 && !template.queries().isEmpty()) {
                    // 获取请求头信息
                    Boolean isPost = Optional.of(template.headers()).map(item -> item.get("ralph-cus-header"))
                            .map(item -> item.contains("post"))
                            .orElse(false);
                    if (isPost){
                        StringBuilder builder = new StringBuilder();
                        Map<String, Collection<String>> queries = template.queries();
                        Iterator<String> queriesIterator = queries.keySet().iterator();

                        while (queriesIterator.hasNext()) {
                            String field = queriesIterator.next();
                            Collection<String> strings = queries.get(field);
                            // 由于参数已经做了url编码处理，这里直接拼接即可
                            builder.append(field + "=" + StringUtils.join(strings, ","));
                            builder.append("&");
                        }
                        template.body(Request.Body.encoded(builder.toString().getBytes(), template.requestCharset()));
                        template.queries(null);
                    }
                }
            }
        };
    }
}
