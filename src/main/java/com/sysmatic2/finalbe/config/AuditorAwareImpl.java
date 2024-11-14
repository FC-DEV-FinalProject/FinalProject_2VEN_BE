package com.sysmatic2.finalbe.config;

import com.sysmatic2.finalbe.member.dto.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    // 현재 사용자의 ID나 이름을 반환.
    // Optional을 사용하여 null이 될 수 있음을 처리
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


//      인증 정보가 없거나 인증이 되어 있지 않으면 빈 Optional을 반환
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String principal = (String) authentication.getPrincipal();
        if (principal.equals("anonymousUser")) {
            return Optional.empty();
        }

        // CustomUserDetails에서 memberId를 가져옴
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return Optional.ofNullable(userDetails.getMemberId());
    }
}