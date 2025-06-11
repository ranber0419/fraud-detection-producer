package com.bank.fraudDetection.mq;

import com.alibaba.fastjson.JSON;
import com.bank.fraudDetection.entity.Transaction;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class FraudDetectionQueueProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${aliyun.kafka.topic}")
    private String transactionsTopic;

    public ResponseEntity<String> send(Transaction tx) {


        kafkaTemplate.send(transactionsTopic, JSON.toJSONString(tx));
        return ResponseEntity.ok("交易已接收");
    }

    public void batchSend(Transaction tx, int messageCount, int threadNum) {
        // 计算每个线程需要发送的消息数量
        int messagesPerThread = messageCount / threadNum;
        // 创建固定大小的线程池
        ExecutorService executor = new ThreadPoolExecutor(threadNum, threadNum, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "kafka-producer-thread-" + threadNumber.getAndIncrement());
                t.setDaemon(false);
                t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());
        long startTime = System.currentTimeMillis();
        // 启动生产者线程
        for (int i = 0; i < threadNum; i++) {
            final int threadId = i;
            executor.submit(() -> {

                int threadSent = 0;
                while (threadSent < messagesPerThread) {
                    // 批量发送消息
                    kafkaTemplate.send(transactionsTopic, JSON.toJSONString(tx));
                    threadSent++;
                }
            });
        }

        // 关闭线程池
        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long duration = endTime -startTime;
        System.out.println("发送时间（毫秒）：" + duration);


    }
}
