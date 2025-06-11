package com.bank.fraudDetection.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Transaction implements Serializable {
    private String id;
    /**
     * 账号
     */
    private String accountNo;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 商户
     */
    private String merchant;
    /**
     * 时间戳
     */
    private Instant timestamp;
}
