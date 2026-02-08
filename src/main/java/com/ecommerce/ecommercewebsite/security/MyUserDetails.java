package com.ecommerce.ecommercewebsite.security;

import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() == null) return Collections.emptyList();
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRole()));
    }


    @Override
    public String getPassword() {
        return user.getPassword(); // Password from User entity
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Using email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // user is always enabled
    }
}
/*
MyUserDetails = Adapter class that provides user details (email, password, role) to Spring Security during authentication.
 */