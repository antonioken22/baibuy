package com.bblets.baibuy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;

import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.models.UserDto;
import com.bblets.baibuy.models.User;
import com.bblets.baibuy.repository.ProductsRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {

    @Autowired
    private ProductsRepository productsRepository;

    @GetMapping("/")
    public String redirectToLanding() {
        return "redirect:/landing/Landing";
    }

    @GetMapping("/landing/Landing")
    public String landingPage() {
        return "landing/Landing";
    }

    @GetMapping("/auth/signin")
    public String loginPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/signup";
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || loggedInUser.getRole() != User.Role.USER) {
            return "redirect:/auth/signin";
        }

        List<Product> products = productsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        Set<String> categories = products.stream()
                .map(Product::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "dashboard/UserDashboard";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        model.addAttribute("user", loggedInUser);

        return "Profile/profile";
    }

    @GetMapping("/password")
    public String passwordPage(Model model, HttpSession session) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    
    if (loggedInUser == null) {
        return "redirect:/login"; 
    }
    
    
    model.addAttribute("user", loggedInUser);
    return "Profile/password"; 
    }

    @GetMapping("/notifications")
    public String notificationsPage(Model model, HttpSession session) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        return "redirect:/login"; 
    }

    model.addAttribute("user", loggedInUser);
    return "Profile/notifications"; 
    }

    @GetMapping("/privacy")
    public String privacySettingsPage(Model model, HttpSession session) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        return "redirect:/login"; 
    }

    model.addAttribute("user", loggedInUser);
    return "Profile/privacy"; 
    }
    
    @GetMapping("/address")
    public String addressPage(Model model, HttpSession session) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        return "redirect:/login"; 
    }

    model.addAttribute("user", loggedInUser);
    return "Profile/address"; 
}


}
