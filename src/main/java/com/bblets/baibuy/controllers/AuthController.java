package com.bblets.baibuy.controllers;

import com.bblets.baibuy.models.User;
import com.bblets.baibuy.models.UserDto;
import com.bblets.baibuy.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserDto userDto, BindingResult result) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            result.rejectValue("email", "error.userDto", "Email is already taken.");
        }

        if (result.hasErrors()) {
            return "auth/signup";
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setImageUrl(userDto.getImageUrl());
        user.setRole(User.Role.USER);
        userRepository.save(user);

        return "redirect:/auth/signin";
    }

    @PostMapping("/signin")
    public String signin(@ModelAttribute UserDto userDto, Model model) {
        return "redirect:/dashboard";
    }

    @GetMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/signin";
    }
}