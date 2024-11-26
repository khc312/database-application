package com.example.database.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_num")
    private Long id;

    @Column(name = "cate_name")
    private String cateName;

    @Column(name = "cate_desc")
    private String cateDesc;

    @Column(name = "cate_active")
    private Boolean active;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @OneToMany(mappedBy = "parentCategory")
    private List<CategoryMiddle> middleCategories; // 중간 카테고리와 연결
}
