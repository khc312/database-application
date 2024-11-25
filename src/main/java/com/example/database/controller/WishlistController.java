package com.example.database.controller;

import com.example.database.entity.User;
import com.example.database.entity.Wishlist;
import com.example.database.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("users/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public String viewWishlist(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // 로그인 필요
        }
        List<Wishlist> wishlist = wishlistService.getWishlist(loggedInUser);
        model.addAttribute("wishlist", wishlist);
        return "wishlist"; // 위시리스트 페이지
    }

    @PostMapping("/add")
    public String addToWishlist(@RequestParam Long productId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            wishlistService.addToWishlist(loggedInUser, productId);
        }
        return "redirect:/users/wishlist";
    }

    @PostMapping("/remove")
    public String removeFromWishlist(@RequestParam Long productId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            wishlistService.removeFromWishlist(loggedInUser, productId);
        }
        return "redirect:/users/wishlist";
    }
    @GetMapping("/wishlist-to-mypage")
    public String goToMyPage() {
        return "redirect:/users/mypage";  // 마이페이지로 리다이렉트
    }
}
