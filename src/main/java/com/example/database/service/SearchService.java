package com.example.database.service;

import com.example.database.entity.Product;
import com.example.database.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 카테고리와 검색어를 기반으로 제품 검색
     *
     * @param category1 1단계 카테고리
     * @param category2 2단계 카테고리
     * @param category3 3단계 카테고리
     * @param category4 4단계 카테고리
     * @param searchQuery 검색어
     * @return 검색된 제품 목록
     */
    public List<Product> searchByCategories(String category1, String category2, String category3, String category4, String searchQuery) {
        return productRepository.findByCategoriesAndSearch(category1, category2, category3, category4, searchQuery);
    }
}
