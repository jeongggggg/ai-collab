package com.aicollab.backend.auth.controller;

import com.aicollab.backend.auth.dto.response.AuthResponse;
import com.aicollab.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/callback")
    public AuthResponse callback(@RequestParam String code) {
        return authService.login(code);
    }
}
