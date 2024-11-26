package com.example.database.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorydetail")
public class CategoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_det_num")
    private Long id;

    @Column(name = "cate_det_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "cate_mid_num") // 중간 카테고리와 연결
    private CategoryMiddle parentCategoryMiddle;
}
