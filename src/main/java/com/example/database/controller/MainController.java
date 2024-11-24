package com.example.database.controller;

import com.example.database.entity.Product;
import com.example.database.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ProductService productService;

    // 기본 메인 페이지
    @GetMapping("/main")
    public String main() {
        return "hello1"; // 메인 화면
    }

    // 검색 로직
    @GetMapping("/main/search")
    public String search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2,
            @RequestParam(required = false) String category3,
            @RequestParam(required = false) String category4,
            Model model
    ) {
        List<Product> products;

        // 이름 기반 검색
        if (name != null && !name.isEmpty()) {
            products = productService.searchByName(name);
            System.out.println("검색된 상품(이름): " + products.size());
        }
        // 카테고리 기반 검색
        else if (category1 != null) {
            if (category2 == null) {
                products = productService.searchByCategory1(category1);
            } else if (category3 == null) {
                products = productService.searchByCategory1And2(category1, category2);
            } else if (category4 == null) {
                products = productService.searchByCategory1And2And3(category1, category2, category3);
            } else {
                products = productService.searchByCategory1And2And3And4(category1, category2, category3, category4);
            }
        }
        // 아무 조건도 없을 경우 전체 상품 조회
        else {
            products = productService.getAllProducts();
            System.out.println("전체 상품: " + products.size());
        }

        // 검색 결과가 없을 경우 메시지 추가
        if (products.isEmpty()) {
            model.addAttribute("message", "검색된 상품이 없습니다.");
        }
        model.addAttribute("products", products);
        return "search_results"; // 검색 결과 화면
    }
}
