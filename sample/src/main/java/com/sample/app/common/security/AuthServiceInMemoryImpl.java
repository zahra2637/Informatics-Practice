package com.sample.app.common.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthServiceInMemoryImpl implements  AuthService{
    private final Map<String , AuthToken> map= new ConcurrentHashMap<>();

    @Override
    public AuthToken getToken(String token) {
        return map.get(token);
    }
}
