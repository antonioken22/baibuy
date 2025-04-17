package com.bblets.baibuy.controllers;

import com.bblets.baibuy.models.User;
import com.bblets.baibuy.models.UserDto;
import com.bblets.baibuy.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
    public String signin(@ModelAttribute UserDto userDto, HttpSession session, Model model) {
        var optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Invalid credentials.");
            return "auth/signin";
        }

        User user = optionalUser.get();

        if (!BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
            model.addAttribute("error", "Invalid credentials.");
            return "auth/signin";
        }

        if (user.isBlocked()) {
            model.addAttribute("error", "This account is blocked.");
            return "auth/signin";
        }

        session.setAttribute("loggedInUser", user);

        return "redirect:/products";
    }

    @GetMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/signin";
    }
}