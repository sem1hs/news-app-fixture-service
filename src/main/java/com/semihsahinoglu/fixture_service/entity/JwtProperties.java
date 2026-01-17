package com.semihsahinoglu.fixture_service.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;


    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(int accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public int getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(int refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

}
