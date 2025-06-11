package com.bank.framework.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${aliyun.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${aliyun.kafka.security-protocol}")
    private String securityProtocol;

    @Value("${aliyun.kafka.sasl-mechanism}")
    private String saslMechanism;

    @Value("${aliyun.kafka.username}")
    private String username;

    @Value("${aliyun.kafka.password}")
    private String password;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 必选配置：阿里云Kafka认证
        configProps.put("security.protocol", securityProtocol);
        configProps.put("sasl.mechanism", saslMechanism);
        configProps.put("sasl.jaas.config", String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                        "username=\"%s\" password=\"%s\";", username, password));

        // 可选配置
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        configProps.put("log4j.logger.org.apache.kafka", "DEBUG");

        configProps.put("ssl.truststore.location", "C:\\code\\git\\gitee\\gitee-yxz\\fraud-detection-producer\\src\\main\\resources\\truststore.jks");
        configProps.put("ssl.truststore.password", "KafkaOnsClient");

        configProps.put("ssl.protocol", "TLSv1.2"); // 强制使用TLSv1.2
        configProps.put("ssl.enabled.protocols", "TLSv1.2"); // 禁用旧版本协议

        // 这会降低安全性，仅限开发和测试环境使用
        configProps.put("ssl.endpoint.identification.algorithm", "");
        configProps.put("auto.create.topics.enable", true);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // KafkaTemplate 实例
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}