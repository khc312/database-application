package com.example.database.repository;

import com.example.database.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 삽입
        Product product1 = new Product();
        product1.setTitle("Test Product 1");
        product1.setCategory1("Electronics");
        product1.setCategory2("Mobile");
        product1.setCategory3("Smartphone");
        product1.setCategory4("Flagship");
        product1.setLprice(100000);
        product1.setHprice(150000);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setTitle("Test Product 2");
        product2.setCategory1("Electronics");
        product2.setCategory2("TV");
        product2.setCategory3("Smart TV");
        product2.setCategory4("OLED");
        product2.setLprice(200000);
        product2.setHprice(300000);
        productRepository.save(product2);
    }

    @Test
    void testFindByCategory1() {
        // 대분류(Category1)로 검색
        List<Product> products = productRepository.findByCategory1("Electronics");
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getCategory1()).isEqualTo("Electronics");
    }

    @Test
    void testFindByCategory1And2() {
        // 대분류 + 중분류 검색
        List<Product> products = productRepository.findByCategory1AndCategory2("Electronics", "Mobile");
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getCategory2()).isEqualTo("Mobile");
    }
}
