package com.sparta.deliveryi.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {

    @Column(name = "user_id")
    private UUID id;

    protected UserId(UUID id) { this.id = id;}

    public static UserId generateId() { return of(UUID.randomUUID()); }

    public static UserId of(UUID id) { return new UserId(id); }

    public String toString() { return id.toString(); }
}
