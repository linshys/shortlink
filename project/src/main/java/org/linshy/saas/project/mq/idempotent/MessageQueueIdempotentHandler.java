package org.linshy.saas.project.mq.idempotent;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 消息队列幂等处理器
 */
@Component
@RequiredArgsConstructor
public class MessageQueueIdempotentHandler {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String IDEMPOTENT_PREFIX = "short-link:idempotent";

    /**
     * 判断消息是否已经被消费
     * @param messageId 消息id标识
     * @return T/F
     */
    public boolean isMessageProcessed(String messageId)
    {
        String key = IDEMPOTENT_PREFIX + messageId;
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, "", 10, TimeUnit.MINUTES));
    }

    /**
     * 删除消息在redis中标识
     */
    public void delMessageProcessed(String messageId)
    {
        String key = IDEMPOTENT_PREFIX + messageId;
        stringRedisTemplate.delete(key);
    }
}
