package com.sparta.deliveryi.transaction.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionId {

    @Column(name = "transaction_id")
    private UUID id;

    protected TransactionId(UUID id) { this.id = id; }

    public static TransactionId generateId() { return of(UUID.randomUUID()); }

    public static TransactionId of(UUID id) { return new TransactionId(id); }

    public UUID toUuid() { return id; }

    public String toString(){ return id.toString(); }
}
