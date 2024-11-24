package com.example.database.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EbayProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 1000)
    private String link;
    @Column(length = 1000)
    private String image;
    private Integer lprice;
    private Integer hprice;
    private String productId;
    private String brand;
    private String category1;
    private String category2;
    private String category3;
    private String currency; // 통화
}
