package com.aicollab.backend.auth.service;

import com.aicollab.backend.auth.dto.response.AuthResponse;
import com.aicollab.backend.auth.dto.response.GithubUserResponse;
import com.aicollab.backend.auth.jwt.JwtTokenProvider;
import com.aicollab.backend.user.domain.User;
import com.aicollab.backend.user.domain.UserRole;
import com.aicollab.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthRestClient oauthClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // Github 인증 전체 로직
    public AuthResponse login(String code) {

        // 1) GitHub Access Token 요청
        String githubAccessToken = oauthClient.requestAccessToken(code);

        // 2) GitHub 유저 정보 요청
        GithubUserResponse githubUser =
                oauthClient.requestUserInfo(githubAccessToken);

        String email = (githubUser.getEmail() == null || githubUser.getEmail().isBlank())
                ? githubUser.getLogin() + "@users.noreply.github.com"
                : githubUser.getEmail();

        // 3) DB 사용자 조회 or 신규 생성
        User user = userRepository.findByGithubId(githubUser.getId())
                .map(existing -> updateExistingUser(existing, githubUser, email, githubAccessToken))
                .orElseGet(() -> createNewUser(githubUser, email, githubAccessToken));

        // 4) JWT 발급
        String jwt = jwtTokenProvider.createAccessToken(user.getId(), user.getLogin());

        // 5) AuthResponse 반환
        return AuthResponse.of(
                jwt,
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getAvatarUrl()
        );
    }

    private User updateExistingUser(
            User user,
            GithubUserResponse github,
            String email,
            String githubAccessToken
    ) {
        user.updateFromGithub(github.getName(), email, github.getAvatarUrl());
        user.updateGithubAccessToken(githubAccessToken);
        user.updateLastLogin();
        return userRepository.save(user);
    }

    private User createNewUser(
            GithubUserResponse github,
            String email,
            String githubAccessToken
    ) {
        User newUser = User.builder()
                .githubId(github.getId())
                .login(github.getLogin())
                .name(github.getName())
                .email(email)
                .avatarUrl(github.getAvatarUrl())
                .githubAccessToken(githubAccessToken)
                .role(UserRole.USER)
                .build();

        return userRepository.save(newUser);
    }
}
