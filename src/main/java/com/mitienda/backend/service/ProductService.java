package com.mitienda.backend.service;

import com.mitienda.backend.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> getAllProducts(int page, int size);

    Page<Product> getByCategoria(String categoria, int page, int size);

    Product getById(Long id);

    Product create(Product product);

    Product update(Long id, Product product);

    void delete(Long id);

    Product updateStock(Long id, Integer nuevoStock);
}
