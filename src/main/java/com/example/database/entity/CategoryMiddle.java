package com.example.database.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorymiddle")
public class CategoryMiddle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_mid_num")
    private Long id;

    @Column(name = "cate_mid_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "cate_num") // 상위 카테고리와 연결
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategoryMiddle")
    private List<CategoryDetail> details; // 세부 카테고리와 연결
}
