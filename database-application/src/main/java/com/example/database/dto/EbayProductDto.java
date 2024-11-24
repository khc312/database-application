package com.example.database.dto;

import com.example.database.entity.EbayProduct;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class EbayProductDto {
    private String title;       // 상품명
    private String link;        // 상품 상세 링크
    private String image;       // 상품 이미지
    private int lprice;         // 최저가
    private int hprice;         // 최고가
    private String productId;   // 상품 ID
    private String brand;       // 브랜드
    private String category1;   // 카테고리 1
    private String category2;   // 카테고리 2
    private String category3;   // 카테고리 3
    private String currency; // 통화
}
