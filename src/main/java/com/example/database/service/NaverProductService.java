package com.example.database.service;

import com.example.database.entity.Product;
import com.example.database.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class NaverProductService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public NaverProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.objectMapper = new ObjectMapper();
    }
    public String fetchProductDataFromNaver(String query) {
        String apiURL = "https://openapi.naver.com/v1/search/shop.json?query=" + query;

        try {
            // URL 객체 생성
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET"); // GET 메서드 설정
            con.setRequestProperty("X-Naver-Client-Id", clientId); // 클라이언트 ID
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret); // 클라이언트 Secret

            int responseCode = con.getResponseCode(); // 응답 코드 확인
            if (responseCode == 200) { // 성공
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                saveProducts(response.toString());
                return response.toString();
            } else { // 실패
                return "Error: API Response Code " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to fetch data from Naver API";
        }

    }
    // 네이버 API 응답 JSON을 파싱하여 데이터베이스에 저장하는 메서드
    public void saveProducts(String jsonResponse) throws Exception {
        // JSON 문자열을 파싱하여 상품 리스트로 변환
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode itemsNode = rootNode.path("items");

        List<Product> products = new ArrayList<>();
        for (JsonNode itemNode : itemsNode) {
            Product product = new Product();
            product.setTitle(itemNode.path("title").asText()); // 상품 이름
            product.setLprice(Integer.parseInt(itemNode.path("lprice").asText())); // 상품 가격
            product.setCategory1(itemNode.path("category1").asText()); // 상품 카테고리
            product.setCategory2(itemNode.path("category2").asText()); // 상품 카테고리
            product.setCategory3(itemNode.path("category3").asText()); // 상품 카테고리
            product.setCategory4(itemNode.path("category4").asText()); // 상품 카테고리
            product.setMallName(itemNode.path("mallName").asText()); // 상품 카테고리
            product.setBrand(itemNode.path("brand").asText());
            product.setMaker(itemNode.path("maker").asText());
            product.setProductId(itemNode.path("productId").asText());
            product.setProductType(itemNode.path("productType").asInt());
            product.setImage(itemNode.path("image").asText());
            product.setLink(itemNode.path("link").asText());
            product.setCurrency("KRW");


            products.add(product);
        }

        // DB에 저장
        productRepository.saveAll(products);
    }
}
