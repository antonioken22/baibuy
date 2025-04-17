package com.bblets.baibuy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                // Allow access to static resources
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**",
                                                                "/libs/**", "/vendor/**", "/Girl.png", "/landing/**",
                                                                "/brandlogos/**")
                                                .permitAll()
                                                // Allow access to public pages
                                                .requestMatchers("/", "/landing/Landing", "/auth/signin").permitAll()
                                                // *** Explicitly allow POST requests to /auth/signup ***
                                                .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                                                // *** Explicitly allow GET requests to /auth/signup ***
                                                .requestMatchers(HttpMethod.GET, "/auth/signup").permitAll()
                                                // Allow access to the product listing for everyone
                                                // Require authentication for other pages
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/auth/signin")
                                                .loginProcessingUrl("/auth/signin")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/products", true)
                                                .failureUrl("/auth/signin?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/signout", "GET"))
                                                .logoutSuccessUrl("/auth/signin?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll());

                return http.build();
        }
}