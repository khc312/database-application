package com.example.database.controller;

import com.example.database.entity.Product;
import com.example.database.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 카테고리 및 검색어를 기반으로 제품 검색
     *
     * @param category1 1단계 카테고리
     * @param category2 2단계 카테고리
     * @param category3 3단계 카테고리
     * @param category4 4단계 카테고리
     * @param searchQuery 검색어
     * @return 검색된 제품 목록
     */
    @GetMapping
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2,
            @RequestParam(required = false) String category3,
            @RequestParam(required = false) String category4,
            @RequestParam(required = false) String searchQuery) {
        List<Product> products = searchService.searchByCategories(category1, category2, category3, category4, searchQuery);
        return ResponseEntity.ok(products);
    }
}
