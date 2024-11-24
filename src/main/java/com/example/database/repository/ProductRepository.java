package com.example.database.repository;

import com.example.database.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitleContaining(String keyword);

    List<Product> findByCategory1(String category1);

    List<Product> findByCategory1AndCategory2(String category1, String category2);

    List<Product> findByCategory1AndCategory2AndCategory3(String category1, String category2, String category3);

    List<Product> findByCategory1AndCategory2AndCategory3AndCategory4(String category1, String category2, String category3, String category4);
}
