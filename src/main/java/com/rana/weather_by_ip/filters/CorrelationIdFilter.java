package com.rana.weather_by_ip.filters;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {
    private static final String CORRELATION_ID = "X-Correlation-ID";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String correlationId = UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID, correlationId); // Store in logging context
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID); // Clean up after request
        }
    }
}
