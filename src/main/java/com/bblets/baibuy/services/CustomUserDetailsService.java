package com.bblets.baibuy.services;

import com.bblets.baibuy.models.User;
import com.bblets.baibuy.repository.UserRepository;
import com.bblets.baibuy.security.AuthUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Authentication Error: User not found with email: " + email));

        return new AuthUserDetails(user);
    }
}
