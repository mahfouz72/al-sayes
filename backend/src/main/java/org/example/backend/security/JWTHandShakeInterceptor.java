package org.example.backend.security;

import org.example.backend.service.NotificationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class JWTHandShakeInterceptor implements HandshakeInterceptor {
    private final JWTService jwtService;
    private UserDetailsManager userDetailsManager;
    private NotificationService notificationService;

    public JWTHandShakeInterceptor(JWTService jwtService, UserDetailsManager userDetailsManager,
                                   @Lazy NotificationService notificationService) {
        this.jwtService = jwtService;
        this.userDetailsManager = userDetailsManager;
        this.notificationService = notificationService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = extractToken(request);
        if (token != null) {
            final String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

            if (jwtService.isValidToken(token, userDetails)) {
                attributes.put("username", username);
                notificationService.setCurrentUserName(username);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                    WebSocketHandler wsHandler, Exception ex) {
    }

    private String extractToken(ServerHttpRequest request) {
        List<String> queryParams = UriComponentsBuilder.fromHttpRequest(request).build().getQueryParams().get("token");
        return (queryParams != null && !queryParams.isEmpty()) ? queryParams.get(0) : null;
    }

}
