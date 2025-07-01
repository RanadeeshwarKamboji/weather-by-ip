package com.rana.weather_by_ip.filters;
import com.rana.weather_by_ip.exceptions.CustomError;
import com.rana.weather_by_ip.exceptions.Details;
import com.rana.weather_by_ip.exceptions.LimitExceededException;
import io.github.bucket4j.*;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@Component
public class RateLimitingFilter implements Filter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws LimitExceededException, IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String ip = req.getRemoteAddr();

        Bucket bucket = buckets.computeIfAbsent(ip, k -> Bucket4j.builder()
                .addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1))))
                .build());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            chain.doFilter(request, response);
        } else {
            long waitForRefillNanos = probe.getNanosToWaitForRefill();
            long retryAfterSeconds = Duration.ofNanos(waitForRefillNanos).getSeconds();

            String message = "{\n" +
                    "    \"status\": 429,\n" +
                    "    \"error\": \"Too Many Requests\",\n" +
                    "    \"message\": \"Rate limit exceeded. Please try again later.\",\n" +
                    "    \"details\": {\n" +
                    "        \"service\": \"WeatherByIP API\",\n" +
                    "        \"issue\": \"Request Limit Exceeded.\",\n" +
                    "        \"suggestion\": \"Reduce your request frequency or check your API plan limits. Refer to 'Retry-After' header for when to retry.\"\n" +
                    "    },\n" +
                    "    \"path\": \"/weather-by-ip\",\n" +
                    "    \"timestamp\": \"2025-07-02T01:03:18.8758589\"\n" +
                    "}";
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());res.setHeader("Retry-After", String.valueOf(retryAfterSeconds)+" Seconds");
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(message);
        }
    }
}