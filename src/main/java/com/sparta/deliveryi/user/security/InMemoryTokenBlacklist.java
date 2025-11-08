package com.sparta.deliveryi.user.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenBlacklist implements TokenBlacklist {

    private Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    @Override
    public void add(String token, Instant expiration) {
        blacklist.put(token, expiration);
    }

    @Override
    public boolean isBlacklisted(String token) {
        Instant expiry = blacklist.get(token);
        if (expiry == null) return false;

        if (expiry.isBefore(Instant.now())) {
            blacklist.remove(token);
            return false;
        }

        return true;
    }
}
