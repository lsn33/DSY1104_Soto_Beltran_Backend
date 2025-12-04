package com.mitienda.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) 
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String categoria;
    @Column(name = "url_imagen")
    private String urlImagen;
}
