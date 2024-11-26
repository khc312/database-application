package com.example.database.service;

import com.example.database.entity.Product;
import com.example.database.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 상품 ID를 기반으로 상품 세부 정보 조회
     *
     * @param productId 상품 ID
     * @return 상품 세부 정보
     */
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }
}
