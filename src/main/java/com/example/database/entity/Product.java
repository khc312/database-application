package com.example.database.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "image")
    private String image;

    @Column(name = "lprice")
    private Integer lprice;

    @Column(name = "hprice")
    private Integer hprice;

    private String mallName;
    private String productId;

    @Column(name = "product_type")
    private Integer productType;

    private String brand;
    private String maker;

    // 추가: 카테고리 필드
    @Column(name = "category1")
    private String category1;

    @Column(name = "category2")
    private String category2;

    @Column(name = "category3")
    private String category3;

    @Column(name = "category4")
    private String category4;

    @ManyToOne
    @JoinColumn(name = "cate_num") // 외래 키로 `Category`와 연결
    private Category category;
}
