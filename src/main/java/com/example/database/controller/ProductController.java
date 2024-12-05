package com.example.database.controller;

import com.example.database.entity.Price;
import com.example.database.entity.Pricemin;
import com.example.database.entity.Product;
import com.example.database.entity.Shop;
import com.example.database.repository.PriceminRepository;
import com.example.database.service.ProductService;
import com.example.database.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/products")
public class ProductController
{
    private final ProductService productService;
    private final ShopService shopService;

    public ProductController(ProductService productService, ShopService shopService)
    {
        this.productService = productService;
        this.shopService = shopService;
    }

    // 상품 상세 페이지
    @GetMapping("/{productId}")
    public String getProductById(@PathVariable Long productId, Model model) {
        Optional<Product> productOpt = productService.getProductByIdWithShop(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            model.addAttribute("product", product);

            List<Product> similarProducts = productService.getProductByTitle(product.getTitle());
            model.addAttribute("similarProducts", similarProducts);

            // 가격 정보가 존재하는지 확인
            if (product.getPrices().isEmpty()) {
                // 가격이 없으면 새로 생성하여 추가
                Price newPrice = new Price();
                newPrice.setPrice(product.getLprice());

                // Product의 mall_name과 Shop의 shop_name을 비교하여 Shop 찾기
                Shop shop = shopService.findShopByShopName(product.getMallName());

                if (shop != null) {
                    newPrice.setShop(shop); // 찾은 Shop을 Price에 설정
                    model.addAttribute("shop",shop);
                } else {
                    // Shop이 없으면 null로 설정 (필요에 따라 예외 처리 가능)
                    newPrice.setShop(null);
                    model.addAttribute("shop", null);
                }

                newPrice.setProduct(product); // 상품과 연결
                // 현재 시간 설정 (priceUpdateDate 및 time 필드)
                newPrice.setTime(LocalDateTime.now());

                // 가격을 저장
                productService.savePrice(newPrice); // price 저장 서비스 메서드 호출
                // 새로 생성된 가격을 모델에 추가
                model.addAttribute("price", newPrice);
                // 가격 정보가 추가되었으므로 상품 상세 페이지로 리다이렉트
                return "redirect:/products/" + productId; // 리다이렉트하여 가격이 반영된 페이지로 이동
            } else {
                // 가격이 있으면 첫 번째 가격을 모델에 추가
                Price firstPrice = product.getPrices().get(0);
                model.addAttribute("price", firstPrice);
                Shop shop = firstPrice.getShop();
                model.addAttribute("shop", shop);
                // 가격 비교: price 테이블의 가격과 lprice가 다를 경우
                if (!firstPrice.getPrice().equals(product.getLprice())) {
                    // 기존 가격을 lprice로 변경
                    firstPrice.setPrice(product.getLprice());
                    firstPrice.setPriceUpdateDate(LocalDateTime.now());

                    // 기존 가격을 pricemin 테이블에 기록
                    Pricemin pricemin = new Pricemin();
                    pricemin.setProKey(product.getId()); // 상품 ID
                    pricemin.setPriceMin(firstPrice.getPrice()); // 새 가격
                    pricemin.setPriceMinTime(LocalDateTime.now()); // 가격 업데이트 시간

                    // 가격 변경 정보를 pricemin에 저장
                    productService.savePricemin(pricemin); // pricemin 저장 서비스 메서드 호출

                    // 가격 업데이트
                    productService.savePrice(firstPrice); // price 저장 서비스 메서드 호출
                }
            }

            return "detail"; // 상세 페이지로 이동
        } else {
            model.addAttribute("error", "Product not found.");
            return "error";
        }
    }
    @GetMapping("/{productId}/price-history")
    @ResponseBody
    public List<Pricemin> getPriceHistory(@PathVariable Long productId) {
        List<Pricemin> priceHistory = productService.getPriceHistory(productId);
        System.out.println("Price history: " + priceHistory); // 디버깅 출력
        return productService.getPriceHistory(productId);
    }



}
