package com.example.database.service;

import com.example.database.entity.Product;
import com.example.database.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void testSearchByCategories() {
        String category1 = "디지털/가전"; // 테스트 데이터
        String category2 = "영상가전";
        String category3 = "TV";
        String category4 = "LEDTV";
        String searchQuery = "LG전자";

        List<Product> products = searchService.searchByCategories(category1, category2, category3, category4, searchQuery);
        assertNotNull(products, "Products should not be null");
    }
}
