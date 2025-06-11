package com.bank.alert.service;

import com.bank.fraudDetection.entity.FraudResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertService {

    public void sendAlert(FraudResult result) {
        // 日志警报 (可扩展为邮件/SMS通知)
        log.warn("欺诈警报! 交易ID: {}, 原因: {}",
                result.getTransactionId(),
                String.join(" | ", result.getReasons()));

        // 可添加集成第三方通知服务
    }
}
