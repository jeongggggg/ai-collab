package com.aicollab.backend.auth.controller;

import com.aicollab.backend.auth.dto.response.AuthResponse;
import com.aicollab.backend.auth.dto.response.UserInfo;
import com.aicollab.backend.auth.security.UserPrincipal;
import com.aicollab.backend.auth.service.AuthService;
import com.aicollab.backend.global.response.ApiResponse;
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
    public UserInfo me(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new UserInfo(
                userPrincipal.getId(),
                userPrincipal.getLogin(),
                userPrincipal.getEmail(),
                userPrincipal.getAvatarUrl()
        );
    }
}
