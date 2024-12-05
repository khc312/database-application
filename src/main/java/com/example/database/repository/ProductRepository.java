package com.example.database.repository;

import com.example.database.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("SELECT DISTINCT p.category1 FROM Product p WHERE p.category1 IS NOT NULL")
    List<String> findDistinctCategory1();


    @Query("SELECT DISTINCT p.category2 FROM Product p WHERE p.category1 = :category1 AND p.category2 IS NOT NULL")
    List<String> findDistinctCategory2ByCategory1(@Param("category1") String category1);


    @Query("SELECT DISTINCT p.category3 FROM Product p WHERE p.category2 = :category2 AND p.category3 IS NOT NULL")
    List<String> findDistinctCategory3ByCategory2(@Param("category2") String category2);


    @Query("SELECT DISTINCT p.category4 FROM Product p WHERE p.category3 = :category3 AND p.category4 IS NOT NULL")
    List<String> findDistinctCategory4ByCategory3(@Param("category3") String category3);


    @Query(value = """
    SELECT * FROM (
        SELECT p.*, ROWNUM AS rn FROM product p
        WHERE p.id = (
                 SELECT MIN(p2.id)
                 FROM product p2
                 WHERE p2.title = p.title
                 AND p2.lprice = (
                     SELECT MIN(p3.lprice)
                     FROM product p3
                     WHERE p3.title = p2.title
                 )
             ) AND
            (:searchQuery IS NULL OR LOWER(p.title) LIKE LOWER('%' || :searchQuery || '%')) AND
            (:category1 IS NULL OR p.category1 = :category1) AND
            (:category2 IS NULL OR p.category2 = :category2) AND
            (:category3 IS NULL OR p.category3 = :category3) AND
            (:category4 IS NULL OR p.category4 = :category4)
        ORDER BY p.ID -- 적절한 정렬 기준을 선택
    )
    WHERE rn > :startRow AND rn <= :endRow
""", nativeQuery = true)
    List<Product> findBySearchAndCategories(
            @Param("searchQuery") String searchQuery,
            @Param("category1") String category1,
            @Param("category2") String category2,
            @Param("category3") String category3,
            @Param("category4") String category4,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow);

    @Query(value = """
    SELECT COUNT(*) FROM product p
            WHERE p.id = (
                 SELECT MIN(p2.id)
                 FROM product p2
                 WHERE p2.title = p.title
                 AND p2.lprice = (
                     SELECT MIN(p3.lprice)
                     FROM product p3
                     WHERE p3.title = p2.title
                 )
             ) AND
        (:searchQuery IS NULL OR LOWER(p.title) LIKE LOWER('%' || :searchQuery || '%')) AND
        (:category1 IS NULL OR p.category1 = :category1) AND
        (:category2 IS NULL OR p.category2 = :category2) AND
        (:category3 IS NULL OR p.category3 = :category3) AND
        (:category4 IS NULL OR p.category4 = :category4)
    """, nativeQuery = true)
    long countBySearchAndCategories(
            @Param("searchQuery") String searchQuery,
            @Param("category1") String category1,
            @Param("category2") String category2,
            @Param("category3") String category3,
            @Param("category4") String category4);

    @Query("SELECT p FROM Product p WHERE p.title = :title")
    List<Product> findByTitle(@Param("title") String title);
}


