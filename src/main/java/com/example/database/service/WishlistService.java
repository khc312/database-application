package com.example.database.service;

import com.example.database.entity.Product;
import com.example.database.entity.User;
import com.example.database.entity.Wishlist;
import com.example.database.repository.ProductRepository;
import com.example.database.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public List<Wishlist> getWishlist(User user) {
        // 위시리스트 목록을 가져온 후 상품 정보를 채워줌
        List<Wishlist> wishlist = wishlistRepository.findByUser(user);

        // 각 Wishlist 항목에 상품 정보 (title, lprice 등) 추가
        for (Wishlist item : wishlist) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            item.setProduct(product);  // 상품 정보를 Wishlist 객체에 설정
        }

        return wishlist;
    }

    public void addToWishlist(User user, Long productId) {
        // 상품과 사용자를 먼저 찾기
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));


        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }

    public void removeFromWishlist(User user, Long productId) {
        wishlistRepository.deleteByUserAndProductId(user, productId);
    }
}
