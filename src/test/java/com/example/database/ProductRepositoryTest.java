package com.example.database;

import com.example.database.entity.Product;
import com.example.database.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByTitleContaining() {
        List<Product> products = productRepository.findByTitleContaining("TV");
        System.out.println("검색된 상품 수: " + products.size());
        for (Product product : products) {
            System.out.println(product.getTitle());
        }
    }

    @Test
    public void testFindByCategory_CateName() {
        List<Product> products = productRepository.findByCategory_CateName("전자기기");
        System.out.println("검색된 상품 수: " + products.size());
        for (Product product : products) {
            System.out.println(product.getTitle());
        }
    }
}
