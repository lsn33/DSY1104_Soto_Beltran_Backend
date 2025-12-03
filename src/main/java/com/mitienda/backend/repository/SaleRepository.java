package com.mitienda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mitienda.backend.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
