package top.sleepnano.kkorder.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisUtil {
    @Autowired
    RedisTemplate redisTemplate;

    public List<String> getKey(String patternKey) throws IOException {
        ScanOptions options = ScanOptions.scanOptions()
                .count(10000) //这里指定每次扫描key的数量
                .match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        while(cursor.hasNext()){
            result.add(cursor.next().toString());
        }
        //切记这里一定要关闭，否则会耗尽连接数。
        cursor.close();
        return result;
    }
}
