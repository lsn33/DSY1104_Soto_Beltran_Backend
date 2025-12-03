package com.mitienda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mitienda.backend.entity.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
}
