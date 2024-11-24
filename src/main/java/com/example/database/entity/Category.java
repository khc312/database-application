package com.example.database.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "category") // DB 테이블 이름
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_num") // DB 컬럼 이름
    private Long id;

    @Column(name = "cate_name") // DB 컬럼 이름
    private String cateName;

    @Column(name = "cate_desc") // DB 컬럼 이름
    private String cateDesc;

    @Column(name = "cate_active") // DB 컬럼 이름
    private Boolean active;

    @OneToMany(mappedBy = "category")
    private List<Product> products; // 카테고리에 속한 제품 목록

    // Getter, Setter, Constructors
}
