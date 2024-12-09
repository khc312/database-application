package com.example.database.service;

import com.example.database.entity.Alarm;
import com.example.database.entity.User;
import com.example.database.repository.AlarmRepository;
import com.example.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public UserService(UserRepository userRepository,AlarmRepository alarmRepository) {
        this.userRepository = userRepository;
        this.alarmRepository = alarmRepository;
    }

    // 인증 처리 시 비밀번호 비교
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        // 비밀번호 매칭 확인
        return password.equals(user.getPassword());
    }
    // 알림 설정 업데이트
    public void updateAlarmConfig(User user, boolean enable) {
        Alarm alarm = alarmRepository.findByUser(user).orElseGet(() -> {
            Alarm newAlarm = new Alarm();
            newAlarm.setUser(user);
            return newAlarm;
        });
        alarm.setAlarmConfig(enable ? 1 : 0);
        alarmRepository.save(alarm);
    }

    // 알림 설정 상태 조회
    public boolean isAlarmEnabled(User user) {
        return alarmRepository.findByUser(user)
                .map(alarm -> alarm.getAlarmConfig() == 1)
                .orElse(false); // 기본값: false
    }
    public boolean getAlarmConfig(User user) {
        return isAlarmEnabled(user); // 기존 isAlarmEnabled 메서드를 그대로 활용
    }

    public User registerUser(User user) {
        // 사용자 정보 저장
        User savedUser = userRepository.save(user);

        // 기본 알림 설정 추가
        Alarm newalarm = new Alarm();
        newalarm.setUser(savedUser);
        newalarm.setAlarmConfig(0);
        alarmRepository.save(newalarm);

        return savedUser;
    }
    // 사용자 정보 업데이트
    public User updateUser(User user) {
        return userRepository.save(user);  // 비밀번호 변경 후 사용자 정보 저장
    }
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);  // 사용자 ID로 사용자 삭제
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
