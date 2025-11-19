package com.aicollab.backend.auth.service;

import com.aicollab.backend.auth.dto.response.AuthResponse;
import com.aicollab.backend.auth.dto.response.GithubUserResponse;
import com.aicollab.backend.user.domain.User;
import com.aicollab.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthRestClient oauthClient;
    private final UserRepository userRepository;

    // Github 인증 전체 로직
    public AuthResponse login(String code){
        // 1) access token 요청
        String accessToken = oauthClient.requestAccessToken(code);

        // 2) Github 사용자 정보 요청
        GithubUserResponse githubUser = oauthClient.requestUserInfo(accessToken);

        // 3) DB에 존재하는 사용자 조회 or 신규 생성
        User user = userRepository.findByGithubId(githubUser.getId())
                .orElseGet(() -> createNewUser(githubUser));

        // 4) JWT (임시 문자열)
        String jwt = "FAKE_JWT_TOKEN";

        return AuthResponse.of(
                jwt,
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getAvatarUrl()
        );
    }

    private User createNewUser(GithubUserResponse user){
        return userRepository.save(
                User.builder()
                        .githubId(user.getId())
                        .login(user.getLogin())
                        .email(user.getEmail())
                        .avatarUrl(user.getAvatarUrl())
                        .build()
        );
    }
}
