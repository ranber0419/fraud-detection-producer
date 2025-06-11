package com.bank.fraudDetection.controller;

import com.bank.domain.vo.Result;
import com.bank.fraudDetection.entity.Transaction;
import com.bank.fraudDetection.mq.FraudDetectionQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Scope("prototype")
@RestController
@RequestMapping("/fraudDetection")
public class FraudDetectionController {

    @Autowired
    private FraudDetectionQueueProducer fraudDetectionQueueProducer;

    @RequestMapping("/send")
    public Result send(@RequestBody Transaction tx) {
        ResponseEntity<String> result = fraudDetectionQueueProducer.send(tx);
        return Result.success(result);
    }

    @RequestMapping("/batchSend")
    public Result batchSend(@RequestBody Transaction tx) {
        int messageCount = 100000;
        int threadNum = 10;
        fraudDetectionQueueProducer.batchSend(tx, messageCount, threadNum);
        return Result.success();
    }
}
