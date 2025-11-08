package com.sparta.deliveryi.order.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends Repository<Order, OrderId> {
    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    @Query("SELECT o FROM Order o " +
            "WHERE (:storeId IS NULL OR o.storeId = :storeId) " +
            "AND (:orderer IS NULL OR o.storeId = :orderer)" +
            "AND o.deletedAt IS NULL")
    Page<Order> search(@Param("storeId") UUID storeId,
                       @Param("orderer") Orderer orderer,
                       Pageable pageable);
}
