package com.example.database.controller;

import com.example.database.service.EbayService;
import com.example.database.service.EbayAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EbayController {

    private final EbayService ebayService;
    private final EbayAuthService ebayAuthService;

    @Autowired
    public EbayController(EbayService ebayService, EbayAuthService ebayAuthService) {
        this.ebayService = ebayService;
        this.ebayAuthService = ebayAuthService;
    }

    @GetMapping("/product-info")
    public String getProductInfo(@RequestParam String searchQuery) {
        // eBay에서 이미 설정된 accessToken을 가져오기
        String accessToken = ebayAuthService.getAccessToken();

        if (accessToken == null || accessToken.isEmpty()) {
            return "Access token is required";
        }

        // 제품 정보 요청
        String productInfo = ebayService.getProductInfo(accessToken, searchQuery);
        if (productInfo.contains("Error")) {
            // 만약 에러가 발생하면 토큰을 갱신 후 재시도
            System.out.println("Refreshing access token...");
            accessToken = ebayAuthService.refreshAccessToken(); // refreshToken을 사용하여 갱신
            productInfo = ebayService.getProductInfo(accessToken, searchQuery);
        }

        return productInfo;
    }

    @GetMapping("/refresh-token")
    public String refreshAccessToken() {
        String newAccessToken = ebayAuthService.refreshAccessToken();

        if (newAccessToken == null) {
            return "Failed to refresh access token";
        }

        return newAccessToken;
    }
}