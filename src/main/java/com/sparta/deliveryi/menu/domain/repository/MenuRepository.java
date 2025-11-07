package com.sparta.deliveryi.menu.domain.repository;

import com.sparta.deliveryi.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.deletedAt IS NULL")
    List<Menu> findAllActive();

    @Query("SELECT m FROM Menu m")
    List<Menu> findAllIncludingDeleted();

    @Query("SELECT m FROM Menu m WHERE m.storeId = :storeId AND m.deletedAt IS NULL")
    List<Menu> findAllByStoreIdAndDeletedAtIsNull(UUID storeId);
}
