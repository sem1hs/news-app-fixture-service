package com.semihsahinoglu.fixture_service.provider;

import com.semihsahinoglu.fixture_service.client.AuthClient;
import org.springframework.stereotype.Component;

@Component
public class ServiceTokenProvider {

    private final AuthClient authClient;

    private String cachedToken;
    private long expiresAt;

    public ServiceTokenProvider(AuthClient authClient) {
        this.authClient = authClient;
    }

    public String getToken() {

        if (cachedToken == null || System.currentTimeMillis() > expiresAt) {
            cachedToken = authClient.getServiceToken();
            expiresAt = System.currentTimeMillis() + 55 * 60 * 1000;
        }

        return cachedToken;
    }
}

