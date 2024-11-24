package com.example.database.service;

import com.example.database.repository.ProductRepository;
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
import com.example.database.entity.EbayProduct;
import com.example.database.repository.EbayProductRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbayService {

    private static final Logger logger = LoggerFactory.getLogger(EbayService.class);

    private final EbayProductRepository ebayproductRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public EbayService(EbayProductRepository ebayproductRepository) {
        this.ebayproductRepository = ebayproductRepository;
        this.objectMapper = new ObjectMapper();
    }


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
            String jsonResponse = response.getBody();
            saveEbayProducts(jsonResponse);
            return jsonResponse;  // 응답 본문을 반환
        } catch (Exception e) {
            logger.error("Error retrieving product info from eBay: {}", e.getMessage());
            return "Error retrieving product info";
        }
    }
    public void saveEbayProducts(String jsonResponse) {
        try {
            // JSON 문자열을 파싱하여 상품 리스트로 변환
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode itemSummariesNode = rootNode.path("itemSummaries");

            List<EbayProduct> products = new ArrayList<>();
            for (JsonNode itemNode : itemSummariesNode) {
                EbayProduct product = new EbayProduct();
                product.setTitle(itemNode.path("title").asText()); // 상품 아이디
                product.setLink(itemNode.path("itemWebUrl").asText()); // 상품 이름
                product.setLprice(itemNode.path("price").path("value").asInt()); // 상품 가격
                product.setCurrency(itemNode.path("price").path("currency").asText()); // 통화

                JsonNode imageNode = itemNode.path("image");
                String imageUrl = imageNode.path("imageUrl").asText();
                product.setImage(imageUrl);

                product.setProductId(itemNode.path("seller").path("username").asText()); // 상품 웹 URL

                JsonNode categoriesNode = itemNode.path("categories");
                String category1 = categoriesNode.size() > 0 ? categoriesNode.get(0).path("categoryName").asText() : null;
                String category2 = categoriesNode.size() > 1 ? categoriesNode.get(1).path("categoryName").asText() : null;
                String category3 = categoriesNode.size() > 2 ? categoriesNode.get(2).path("categoryName").asText() : null;

                product.setCategory1(category1);
                product.setCategory2(category2);
                product.setCategory3(category3);

                products.add(product);
            }

            // DB에 저장
            ebayproductRepository.saveAll(products);

        } catch (Exception e) {
            logger.error("Error saving eBay products to DB: {}", e.getMessage());
        }
    }
}
