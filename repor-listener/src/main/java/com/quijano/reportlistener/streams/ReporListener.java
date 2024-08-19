package com.quijano.reportlistener.streams;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class ReporListener {

    @Bean
    public Consumer<String> consumerReport(){
        return report -> {
            log.info("Received report: {}", report);
        };
    }
}
