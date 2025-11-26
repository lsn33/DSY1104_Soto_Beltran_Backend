package com.mitienda.backend.service.impl;

import com.mitienda.backend.entity.Product;
import com.mitienda.backend.repository.ProductRepository;
import com.mitienda.backend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<Product> getAllProducts(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Product> getByCategoria(String categoria, int page, int size) {
        return repo.findByCategoria(categoria, PageRequest.of(page, size));
    }

    @Override
    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public Product create(Product product) {
        return repo.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product original = getById(id);

        original.setNombre(product.getNombre());
        original.setDescripcion(product.getDescripcion());
        original.setPrecio(product.getPrecio());
        original.setStock(product.getStock());
        original.setCategoria(product.getCategoria());

        return repo.save(original);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Product updateStock(Long id, Integer nuevoStock) {
        Product p = getById(id);
        p.setStock(nuevoStock);
        return repo.save(p);
    }
}
