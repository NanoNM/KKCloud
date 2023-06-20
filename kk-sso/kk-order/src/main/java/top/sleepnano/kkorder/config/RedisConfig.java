package top.sleepnano.kkorder.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

import static org.bouncycastle.cms.RecipientId.password;

/**
 * 使用FastJson序列化Redis
 */
@Configuration
public class RedisConfig {

    @Value("${kk.redis.database.orderDB}")
    private int orderDatabase;


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //key值使用spring默认的StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value值使用fastjson的GenericFastJsonRedisSerializer
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        //以下是hash序列化的配置
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        // redisTemplate
        return redisTemplate;
    }

//    private  RedisTemplate<String, Object> getStringRedisTemplate(int database) {
//        RedisTemplate<String, Object> template = new RedisTemplate();
//        // 构建工厂对象
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setHostName(host);
//        configuration.setPort(port);
//        configuration.setPassword(RedisPassword.of(password));
//        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
//                .commandTimeout(Duration.ofSeconds(timeout)).poolConfig(getPoolConfig()).build();
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, clientConfiguration);
//        // 设置使用的redis数据库
//
//
//        // 重新初始化工厂
//        factory.afterPropertiesSet();
//        template.setConnectionFactory(factory);
//        return template;
//    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 监听所有库的key过期事件
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
