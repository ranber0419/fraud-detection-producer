package com.bank.fraudDetection.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@ToString
public class FraudResult {

    private final String transactionId;
    private final boolean isFraud;
    private final List<String> reasons;
    private final Instant timestamp;

    // 可选：添加业务方法
    public String getFormattedReasons() {
        return reasons != null ? String.join("; ", reasons) : "";
    }
}
