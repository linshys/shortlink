package org.linshy.saas.project.mq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.linshy.saas.project.common.convention.exception.ServiceException;
import org.linshy.saas.project.dto.biz.ShortLinkStatsRecordDTO;
import org.linshy.saas.project.mq.idempotent.MessageQueueIdempotentHandler;
import org.linshy.saas.project.service.ShortLinkService;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import static org.linshy.saas.project.common.constant.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;

/**
 * 延迟记录短链接统计组件
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsConsumer implements InitializingBean {

    private final RedissonClient redissonClient;
    private final ShortLinkService shortLinkService;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;
    public void onMessage()
    {
        Executors.newSingleThreadExecutor(
                // 定义线程属性
                runnable ->{
                    Thread thread = new Thread(runnable);
                    thread.setName("delay_short-link_stats_consumer");
                    // 守护线程，主程序结束，守护线程立即结束，防止不同步
                    thread.setDaemon(Boolean.TRUE);
                    return thread;
                })
                .execute(()-> {
                    RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
                    RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
                    for (;;)
                    {
                        try {
                            ShortLinkStatsRecordDTO statsRecordDTO = delayedQueue.poll();
                            if (statsRecordDTO!=null)
                            {
                                if (!messageQueueIdempotentHandler.isMessageProcessed(statsRecordDTO.getKeys())) {
                                    if (messageQueueIdempotentHandler.isAccomplish(statsRecordDTO.getKeys()))
                                        return;
                                    throw new ServiceException("消息未完成流程，消息队列重试");
                                }
                                try {
                                    shortLinkService.shortLinkStats(null, null, statsRecordDTO);
                                }
                                catch (Throwable ex)
                                {
                                    messageQueueIdempotentHandler.delMessageProcessed(statsRecordDTO.getKeys());
                                    log.error("延迟记录短链接监控消费异常", ex);
                                }
                                messageQueueIdempotentHandler.setAccomplish(statsRecordDTO.getKeys());
                                continue;

                            }
                            LockSupport.parkUntil(500);
                        }
                        catch (Throwable ignored)
                        {

                        }
                    }
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        onMessage();
    }
}
