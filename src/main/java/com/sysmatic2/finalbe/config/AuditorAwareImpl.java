package com.sysmatic2.finalbe.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    // 현재 사용자의 ID나 이름을 반환.
    // Optional을 사용하여 null이 될 수 있음을 처리
    public Optional<Long> getCurrentAuditor() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 인증이 되어 있지 않으면 빈 Optional을 반환
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }

        // 인증된 사용자의 이름 반환 (username 또는 ID)
//        return Optional.of(authentication.getName());

        // 실제 구현에서 인증된 사용자 ID를 반환하도록 설정 (예: 1L은 임시 사용자 ID)
        System.out.println("AuditorAwareImpl 호출");
        return Optional.of(1L);
    }
}
