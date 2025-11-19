package com.aicollab.backend.auth.security;

import com.aicollab.backend.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword(){
        return null; // OAuth 로그인
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override public boolean isAccountNonExpired() { return true;}
    @Override public boolean isAccountNonLocked() { return true;}
    @Override public boolean isCredentialsNonExpired() { return true;}
    @Override public boolean isEnabled() { return true;}
}
