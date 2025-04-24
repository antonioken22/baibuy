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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                // Public static assets
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**",
                                                                "/libs/**", "/vendor/**", "/Girl.png", "/landing/**",
                                                                "/brandlogos/**")
                                                .permitAll()

                                                // Public pages
                                                .requestMatchers("/", "/landing/Landing", "/auth/signin").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/auth/signup").permitAll()

                                                // Authenticated pages
                                                .requestMatchers("/dashboard/**", "/profile/**").authenticated()
                                                .requestMatchers("/products/create/**", "/products/edit/**",
                                                                "/products/delete/**")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.POST, "/api/messages/send").authenticated()

                                                // Everything else
                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/auth/signin")
                                                .loginProcessingUrl("/auth/signin")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/dashboard", true)
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