package com.aicollab.backend.auth.controller;

import com.aicollab.backend.auth.dto.response.AuthResponse;
import com.aicollab.backend.auth.dto.response.UserInfo;
import com.aicollab.backend.auth.service.AuthService;
import com.aicollab.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/exchange")
    public AuthResponse exchange(@RequestParam String code) {
        return authService.login(code);
    }

    @GetMapping("/me")
    public UserInfo me(@AuthenticationPrincipal User user) {
        if (user == null) {
            // Security 필터에서 인증 안 된 상태면 여기 안 들어오게 설정돼 있음
            return null;
        }
        return new UserInfo(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getAvatarUrl()
        );
    }
}
