package com.example.database.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EbayService {

    private static final Logger logger = LoggerFactory.getLogger(EbayService.class);

    @Value("${ebay.api.url}")
    private String ebayApiUrl;

    public String getProductInfo(String accessToken, String searchQuery) {
        RestTemplate restTemplate = new RestTemplate();

        // searchQuery URL 인코딩
        String encodedSearchQuery = null;
        try {
            encodedSearchQuery = URLEncoder.encode(searchQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error encoding search query: {}", e.getMessage());
            return "Error encoding search query";
        }

        // API 호출 URL (검색어를 동적으로 처리)
        String apiUrl = ebayApiUrl + "/buy/browse/v1/item_summary/search?q=" + encodedSearchQuery;

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // HTTP GET 요청을 보냄
        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            return response.getBody();  // 응답 본문을 반환
        } catch (Exception e) {
            logger.error("Error retrieving product info from eBay: {}", e.getMessage());
            return "Error retrieving product info";
        }
    }
}