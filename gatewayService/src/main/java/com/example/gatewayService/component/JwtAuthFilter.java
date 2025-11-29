package com.example.gatewayService.component;

import com.example.gatewayService.dto.UserValidationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter implements GlobalFilter {

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Public routes (no token required)
    private static final List<String> PUBLIC_ROUTES = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/admin-login",
            "/api/v1/auth/signup",
            "/api/v1/validate",
            "/api/v1/public",
            "/api/v1/stream"
    );

    // Role-based protected routes
    private static final Map<String, List<String>> ROLE_BASED_ROUTES = Map.of(
            "/api/v1/series", List.of("Admin"),
            "/api/v1/user-series", List.of("USER", "Admin")
    );

    private boolean isPublicRoute(String path) {
        return PUBLIC_ROUTES.stream().anyMatch(path::startsWith);
    }

    private boolean isAuthorized(String path, String userRole) {
        for (var entry : ROLE_BASED_ROUTES.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue().contains(userRole);
            }
        }
        return true; // if no rule found, route is allowed
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Skip for public routes
        if (isPublicRoute(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);



        return webClientBuilder.build()
                .post()
                .uri("http://192.168.0.109:8080/api/v1/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        res -> Mono.error(new RuntimeException("Invalid Token")))
                .bodyToMono(UserValidationResponse.class) // parse JSON
                .flatMap(user -> {

                    String userRole = user.getRole();

                    if (!isAuthorized(path, userRole)) {
                        return forbidden(exchange);
                    }

                    ServerWebExchange mutated = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .header("X-User-Name", user.getUsername())
                                    .header("X-User-Role", userRole)
                                    .build())
                            .build();

                    return chain.filter(mutated);

                })
                .onErrorResume(e -> unauthorized(exchange));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}

