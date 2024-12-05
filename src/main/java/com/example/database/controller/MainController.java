package com.example.database.controller;

import com.example.database.entity.Product;
import com.example.database.entity.User;
import com.example.database.service.SearchHistoryService;
import com.example.database.service.SearchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {


    @Autowired
    private SearchService searchService;

    // 메인 페이지
    @GetMapping("/main")
    public String main(
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2,
            @RequestParam(required = false) String category3,
            @RequestParam(required = false) String category4,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpSession session,
            Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 전체 상품 수 또는 검색된 상품 수 계산
        long totalItems = searchService.countSearchResults(searchQuery, category1, category2, category3, category4);

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // 검색된 상품 리스트 조회
        List<Product> products = searchService.searchProducts(searchQuery, category1, category2, category3, category4, page, size);

        // 검색된 상품이 없을 경우 메시지 추가
        if (products.isEmpty()) {
            model.addAttribute("message", "검색된 상품이 없습니다.");
            // 상품이 없으면 첫 페이지로 되돌리기
            page = 0;
            products = searchService.searchProducts(searchQuery, category1, category2, category3, category4, page, size);
            totalItems = searchService.countSearchResults(searchQuery, category1, category2, category3, category4);
            totalPages = (int) Math.ceil((double) totalItems / size);
        }

        // 페이지 네비게이션 시작 페이지와 끝 페이지 계산
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);

        // 모델에 필요한 속성 추가
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("loggedInUser", loggedInUser);
        // 검색 결과 화면으로 리턴
        return "search_results";
    }
}
