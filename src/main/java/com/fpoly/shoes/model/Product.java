package com.fpoly.shoes.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "nvarchar(255)")
    private String name;
    @Column(columnDefinition = "nvarchar(255)")
    private String description;
    private double price;
    private int quantity;
    private String image;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }


}
