package com.example.database.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist")
@Data
@NoArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wishlist_seq")
    @SequenceGenerator(name = "wishlist_seq", sequenceName = "WISHLIST_SEQ", allocationSize = 1)
    @Column(name = "WISH_NUM")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 위시리스트는 특정 사용자와 연결됩니다.

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 상품 ID 저장
}
