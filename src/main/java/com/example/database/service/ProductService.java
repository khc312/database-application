package com.example.database.service;

import com.example.database.entity.Price;
import com.example.database.entity.Pricemin;
import com.example.database.entity.Product;
import com.example.database.entity.Shop;
import com.example.database.repository.PriceRepository;
import com.example.database.repository.PriceminRepository;
import com.example.database.repository.ProductRepository;
import com.example.database.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final PriceminRepository priceminRepository;

    public ProductService(ProductRepository productRepository, PriceRepository priceRepository, PriceminRepository priceminRepository) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.priceminRepository = priceminRepository;
    }

    public List<Product> getProductByTitle(String title) {
        return productRepository.findByTitle(title);
    }


    @Transactional
    public Optional<Product> getProductByIdWithShop(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        productOpt.ifPresent(product -> {
            // 가격 정보와 연관된 Shop 정보를 초기화
            product.getPrices().forEach(price -> price.getShop());
        });
        return productOpt;
    }

    public void savePrice(Price price) {
        // 가격을 저장
        priceRepository.save(price);
    }
    // 가격 변경 기록 저장 (Pricemin 테이블에)
    public void savePricemin(Pricemin pricemin) {
        priceminRepository.save(pricemin);
    }

    public List<Pricemin> getPriceHistory(Long productId) {
        return priceminRepository.findByProKeyOrderByPriceMinTimeDesc(productId);
    }


}