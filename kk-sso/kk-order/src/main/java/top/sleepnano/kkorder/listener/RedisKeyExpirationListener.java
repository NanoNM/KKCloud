package top.sleepnano.kkorder.listener;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import top.sleepnano.kkorder.service.OrderService;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    OrderService orderService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     */
    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        log.info("监听Redis key过期，key：{}，channel：{}", message, new String(pattern));
        if (message.toString().contains("order:")){
            orderService.orderCacheExpired(message.toString());
            log.info("订单操作完成 key: {}",message);
        }

    }
}
