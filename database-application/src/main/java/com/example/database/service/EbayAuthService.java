package com.example.database.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import java.util.Base64;
import java.util.Map;

@Service
public class EbayAuthService {

    @Value("${ebay.client.id}")
    private String clientId;

    @Value("${ebay.client.secret}")
    private String clientSecret;

    @Value("${ebay.api.url}")
    private String ebayApiUrl;

    @Value("${ebay.api.refresh_token}")
    private String refreshToken;

    @Value("${ebay.api.access_token}")
    private String accessToken;

    // client_credentials를 사용한 Access Token 발급
    public String getAccessToken() {
        // 이미 application.properties에서 설정된 access_token을 사용
        if (accessToken != null && !accessToken.isEmpty()) {
            accessToken = refreshAccessToken();
            return accessToken;
        }

        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = ebayApiUrl + "/identity/v1/oauth2/token";

        // Base64로 client_id와 client_secret을 인코딩
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Authorization", "Basic " + encodedAuth);

        // 요청 body 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "https://api.ebay.com/oauth/api_scope https://api.ebay.com/oauth/api_scope/buy.item.bulk");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            // POST 요청 보내기
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                assert responseBody != null;
                return (String) responseBody.get("access_token");
            } else {
                // 오류 처리
                System.out.println("Error while fetching access token: " + response.getBody());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // refresh_token을 사용하는 Access Token 갱신 메소드
    public String refreshAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = ebayApiUrl + "/identity/v1/oauth2/token";

        // Base64로 client_id와 client_secret 인코딩
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Authorization", "Basic " + encodedAuth);

        // 요청 body 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        body.add("scope", "https://api.ebay.com/oauth/api_scope");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            // POST 요청 보내기
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                assert responseBody != null;
                return (String) responseBody.get("access_token");
            } else {
                // 오류 처리
                System.out.println("Error while refreshing access token: " + response.getBody());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
