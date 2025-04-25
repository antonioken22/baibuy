package com.bblets.baibuy.security;

import com.bblets.baibuy.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AuthUserDetails implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;
    private final User.Role role;

    public AuthUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.isEnabled = !user.isBlocked();
        this.authorities = mapRolesToAuthorities(user.getRole());
        this.role = user.getRole();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(User.Role role) {
        if (role == null) {
            return Collections.emptyList();
        }
        // Spring Security expects roles in the format "ROLE_USER", "ROLE_ADMIN" etc.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return isEnabled;
    }

    public User.Role getRole() {
        return role;
    }
}
