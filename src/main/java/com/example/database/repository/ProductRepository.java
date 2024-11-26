package com.example.database.repository;

import com.example.database.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:category1 IS NULL OR p.category1 = :category1) AND " +
            "(:category2 IS NULL OR p.category2 = :category2) AND " +
            "(:category3 IS NULL OR p.category3 = :category3) AND " +
            "(:category4 IS NULL OR p.category4 = :category4) AND " +
            "(:searchQuery IS NULL OR p.title LIKE %:searchQuery%)")
    List<Product> findByCategoriesAndSearch(
            @Param("category1") String category1,
            @Param("category2") String category2,
            @Param("category3") String category3,
            @Param("category4") String category4,
            @Param("searchQuery") String searchQuery
    );
}
