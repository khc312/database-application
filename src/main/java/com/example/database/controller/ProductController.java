package com.example.database.controller;

import com.example.database.entity.Product;
import com.example.database.service.ProductService;
import com.example.database.service.NaverProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final NaverProductService naverProductService;

    @Autowired
    public ProductController(ProductService productService, NaverProductService naverProductService) {
        this.productService = productService;
        this.naverProductService = naverProductService;
    }

    // 네이버 API 상품 검색
    @GetMapping("/products/search")
    public ResponseEntity<String> searchProducts(@RequestParam String query) {
        try {
            String response = naverProductService.fetchProductDataFromNaver(query);
            return ResponseEntity.ok(response); // 성공 시 JSON 응답 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: Unable to fetch data");
        }
    }

    // 카테고리 검색
    @GetMapping("/products/search/category")
    public ResponseEntity<List<Product>> searchByCategory(
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2,
            @RequestParam(required = false) String category3,
            @RequestParam(required = false) String category4) {
        try {
            List<Product> products;
            if (category1 != null && category2 == null) {
                products = productService.searchByCategory1(category1);
            } else if (category1 != null && category2 != null && category3 == null) {
                products = productService.searchByCategory1And2(category1, category2);
            } else if (category1 != null && category2 != null && category3 != null && category4 == null) {
                products = productService.searchByCategory1And2And3(category1, category2, category3);
            } else if (category1 != null && category2 != null && category3 != null && category4 != null) {
                products = productService.searchByCategory1And2And3And4(category1, category2, category3, category4);
            } else {
                products = productService.getAllProducts(); // 기본 전체 조회
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
