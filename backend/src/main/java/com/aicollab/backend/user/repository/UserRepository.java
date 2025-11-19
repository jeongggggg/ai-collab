package com.aicollab.backend.user.repository;

import com.aicollab.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGithubId(String githubId);
    Optional<User> findByLogin(String login);
}
