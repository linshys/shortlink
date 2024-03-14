package org.linshy.saas.project.mq.producer;

import lombok.RequiredArgsConstructor;
import org.linshy.saas.project.dto.biz.ShortLinkStatsRecordDTO;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.linshy.saas.project.common.constant.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;

/**
 * 延迟消息短链接统计发送者
 */
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsProducer {

    private final RedissonClient redissonClient;

    public void send(ShortLinkStatsRecordDTO statsRecord)
    {
        RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
        RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        // 消息5s后才能被消费
        delayedQueue.offer(statsRecord, 5 , TimeUnit.SECONDS);
    }


}
