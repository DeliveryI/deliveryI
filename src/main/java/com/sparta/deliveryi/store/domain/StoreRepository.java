package com.sparta.deliveryi.store.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends Repository<Store, StoreId> {
    Store save(Store store);

    Optional<Store> findById(StoreId storeId);

    List<Store> findAllByOwnerAndDeletedAtIsNull(Owner owner);

    @Query("""
                    SELECT s FROM Store s
                    WHERE (:owner IS NULL OR s.owner = :owner)
                    AND (
                        :keyword IS NULL OR (
                            s.name LIKE %:keyword%
                            OR s.description LIKE %:keyword%
                            OR s.category.name LIKE %:keyword%
                        )
                    )
                    AND (:includeDeleted = TRUE OR s.deletedAt IS NULL)
            """)
    Page<Store> search(@Param("owner") Owner owner,
                       @Param("keyword") String keyword,
                       @Param("includeDeleted") boolean includeDeleted,
                       Pageable pageable);
}
