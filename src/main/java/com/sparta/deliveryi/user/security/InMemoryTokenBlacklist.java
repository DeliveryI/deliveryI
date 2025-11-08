package com.sparta.deliveryi.user.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class InMemoryTokenBlacklist implements TokenBlacklist {

    private Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    @Override
    public void add(String token, Instant expiration) {
        blacklist.put(token, expiration);
        log.info("[AccessToken blacklist] Token: " + token + " expiration: " + expiration);
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
