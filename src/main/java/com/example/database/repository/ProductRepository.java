package com.example.database.repository;

import com.example.database.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 상품 이름에 키워드가 포함된 상품 조회
    List<Product> findByTitleContaining(String keyword);

    // 가격으로 검색
    List<Product> findByLprice(int lprice);


    List<Product> findByCategory_CateName(String categoryName);

    // 특정 대분류(Category1)에 해당하는 제품 조회
    List<Product> findByCategory1(String category1);

    // 대분류(Category1) + 중분류(Category2)
    List<Product> findByCategory1AndCategory2(String category1, String category2);

    // 대분류(Category1) + 중분류(Category2) + 소분류(Category3)
    List<Product> findByCategory1AndCategory2AndCategory3(String category1, String category2, String category3);

    // 대분류(Category1) + 중분류(Category2) + 소분류(Category3) + 세부분류(Category4)
    List<Product> findByCategory1AndCategory2AndCategory3AndCategory4(String category1, String category2, String category3, String category4);
}
