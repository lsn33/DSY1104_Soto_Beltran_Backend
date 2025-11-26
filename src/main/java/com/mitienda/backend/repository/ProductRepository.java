package com.mitienda.backend.repository;

import com.mitienda.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoria(String categoria, Pageable pageable);
}
