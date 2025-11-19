package com.aicollab.backend.auth.controller;

import com.aicollab.backend.auth.dto.response.AuthResponse;
import com.aicollab.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/github")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/exchange")
    public AuthResponse exchange(@RequestParam String code) {
        return authService.login(code);
    }
}
