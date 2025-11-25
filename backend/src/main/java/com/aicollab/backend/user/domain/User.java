package com.aicollab.backend.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Github 내부 ID
    @Column(nullable = false, unique = true)
    private Long githubId;

    @Column(nullable = false, length= 100)
    private String login;

    private String name;
    private String email;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    @Column(name = "github_access_token")
    private String githubAccessToken;

    public void updateGithubAccessToken(String token) {
        this.githubAccessToken = token;
    }

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if(this.role == null) this.role = UserRole.USER;
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    // Github 정보 변경 시 업데이트
    public void updateFromGithub(String name, String email, String avatarUrl){
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public void updateLastLogin(){
        this.lastLoginAt = LocalDateTime.now();
    }
}
