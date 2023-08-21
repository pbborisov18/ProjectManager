package com.company.projectManager.common.security;

import com.company.projectManager.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class SecurityUser implements UserDetails {

    private User user;
    private List<GrantedAuthority> roles;

    public SecurityUser(User user, List<GrantedAuthority> roles) {
        this.user = user;
        this.roles = roles;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
