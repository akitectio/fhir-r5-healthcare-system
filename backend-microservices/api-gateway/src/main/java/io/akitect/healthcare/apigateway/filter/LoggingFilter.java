package io.akitect.healthcare.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = UUID.randomUUID().toString();

        log.info("Request: {} {} {} requestId: {}",
                request.getMethod(),
                request.getURI(),
                request.getQueryParams(),
                requestId);

        // Add request ID to response header for correlation
        return chain.filter(
                        exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("X-Request-ID", requestId)
                                        .build())
                                .build())
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    log.info("Response: status={}, requestId={}",
                            response.getStatusCode(),
                            requestId);
                }));
    }
}