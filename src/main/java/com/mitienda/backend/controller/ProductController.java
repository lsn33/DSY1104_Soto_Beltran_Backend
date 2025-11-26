package com.mitienda.backend.controller;

import com.mitienda.backend.entity.Product;
import com.mitienda.backend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // GET paginado
    @GetMapping
    public Page<Product> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return service.getAllProducts(page, size);
    }

    // GET por categoría
    @GetMapping("/categoria")
    public Page<Product> getByCategoria(
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return service.getByCategoria(categoria, page, size);
    }

    // GET por ID
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Crear producto
    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.create(product);
    }

    // Actualizar producto completo
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return service.update(id, product);
    }

    // Borrar producto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // PATCH stock
    @PatchMapping("/{id}/stock")
    public Product updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock
    ) {
        return service.updateStock(id, stock);
    }
}
