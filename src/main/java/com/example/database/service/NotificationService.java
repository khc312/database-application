package com.example.database.service;

import com.example.database.dto.NotificationDTO;
import com.example.database.entity.Alarm;
import com.example.database.entity.User;
import com.example.database.entity.Wishlist;
import com.example.database.repository.AlarmRepository;
import com.example.database.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final WishlistRepository wishlistRepository;
    private final EmailService emailService;
    private final AlarmRepository alarmRepository;


    @Autowired
    public NotificationService(WishlistRepository wishlistRepository, EmailService emailService,
                               AlarmRepository alarmRepository) {
        this.wishlistRepository = wishlistRepository;
        this.emailService = emailService;
        this.alarmRepository = alarmRepository;
    }

    @Scheduled(fixedRate = 100000)
    public void checkPriceDrops() {
        List<Wishlist> wishlistItems = wishlistRepository.findAll();
        System.out.println("Wishlist 조회 완료. 총 아이템 수: " + wishlistItems.size());
        for (Wishlist item : wishlistItems) {
            
            Alarm alarm = alarmRepository.findByUser(item.getUser())
                    .orElseThrow(() -> new RuntimeException("Alarm settings not found for user: " + item.getUser().getId()));
            if (alarm == null) {
                System.out.println("알림 설정이 없는 사용자: " + item.getUser().getId());
                continue;
            }

            if (alarm.getAlarmConfig() == 1 && // 알림 설정이 활성화된 경우
                    item.getDesiredPrice() != null &&
                    item.getDesiredPrice() >= item.getProduct().getLprice()) {
                sendPriceDropNotification(item);
                System.out.println("알림 조건 만족: " + item.getProduct().getTitle());
            }
        }
    }
    public void sendNotificationEmail(String userEmail, String subject, String body) {
        emailService.sendEmail(userEmail, subject, body);
    }

    private void sendPriceDropNotification(Wishlist item) {
        String message = String.format("상품 '%s'의 가격이 희망 가격 %d원 이하로 떨어졌습니다! 현재 가격: %d원",
                item.getProduct().getTitle(),
                item.getDesiredPrice(),
                item.getProduct().getLprice());

        String email = item.getUser().getEmail();
        sendNotificationEmail(email, "가격 하락 알림", message);
    }

    public List<NotificationDTO> getNotificationsForUser(User user) {
        // 위시리스트에서 사용자에게 표시할 알림 데이터 가져오기
        List<Wishlist> wishlistItems = wishlistRepository.findByUser(user);
        List<NotificationDTO> notifications = new ArrayList<>();

        for (Wishlist item : wishlistItems) {
            if (item.getDesiredPrice() != null && item.getDesiredPrice() >= item.getProduct().getLprice()) {
                notifications.add(new NotificationDTO("상품 '" + item.getProduct().getTitle() +
                        "'의 가격이 희망 가격 이하로 떨어졌습니다! 현재 가격: " + item.getProduct().getLprice() + "원"));
            }
        }

        return notifications;
    }
}

