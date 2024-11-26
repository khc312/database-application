package com.example.database.controller;

import com.example.database.entity.Product;
import com.example.database.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 특정 상품의 세부 정보 조회
     *
     * @param productId 상품 ID
     * @return 상품 세부 정보
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductDetails(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
