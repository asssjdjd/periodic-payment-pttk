package com.example.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String correlationId = MDC.get("correlationId");
        if (correlationId != null) {
            requestTemplate.header("X-Correlation-ID", correlationId);
        }
    }
}