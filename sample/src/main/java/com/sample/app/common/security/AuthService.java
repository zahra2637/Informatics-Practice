package com.sample.app.common.security;

public interface AuthService {
    AuthToken getToken(String token);
}
