package com.sparta.deliveryi.user.security;

import java.time.Instant;

public interface TokenBlacklist {
    void add(String token, Instant expiration);
    boolean isBlacklisted(String token);
}
