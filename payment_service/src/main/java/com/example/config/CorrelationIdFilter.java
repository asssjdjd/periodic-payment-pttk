package com.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_LOG_VAR = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Đọc ID từ Header do Kong truyền xuống
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        // 2. Fallback trường hợp gọi trực tiếp không qua Kong
        if (!StringUtils.hasText(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        try {
            // 3. Đưa vào MDC
            MDC.put(CORRELATION_ID_LOG_VAR, correlationId);

            // 4. (Tùy chọn) Trả lại ID trong response
            response.setHeader(CORRELATION_ID_HEADER, correlationId);

            filterChain.doFilter(request, response);
        } finally {
            // 5. Xóa khỏi MDC để tránh rò rỉ bộ nhớ (Memory Leak) giữa các thread trong ThreadPool
            MDC.remove(CORRELATION_ID_LOG_VAR);
        }
    }
}