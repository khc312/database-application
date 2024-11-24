package com.example.database.service;

import com.example.database.entity.Product;
import com.example.database.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> searchByName(String name) {
        return productRepository.findByTitleContaining(name);
    }

    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategory_CateName(category);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 대분류(Category1) 검색
    public List<Product> searchByCategory1(String category1) {
        return productRepository.findByCategory1(category1);
    }

    // 대분류 + 중분류(Category1 + Category2)
    public List<Product> searchByCategory1And2(String category1, String category2) {
        return productRepository.findByCategory1AndCategory2(category1, category2);
    }

    // 대분류 + 중분류 + 소분류(Category1 + Category2 + Category3)
    public List<Product> searchByCategory1And2And3(String category1, String category2, String category3) {
        return productRepository.findByCategory1AndCategory2AndCategory3(category1, category2, category3);
    }

    // 대분류 + 중분류 + 소분류 + 세부분류(Category1 + Category2 + Category3 + Category4)
    public List<Product> searchByCategory1And2And3And4(String category1, String category2, String category3, String category4) {
        return productRepository.findByCategory1AndCategory2AndCategory3AndCategory4(category1, category2, category3, category4);
    }
}
