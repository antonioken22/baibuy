package com.bblets.baibuy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.bblets.baibuy.security.AuthUserDetails;

import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.models.UserDto;
import com.bblets.baibuy.models.User;
import com.bblets.baibuy.repository.ProductsRepository;
import com.bblets.baibuy.repository.UserRepository;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UserRepository userRepository;

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
    public String userDashboard(Model model, @AuthenticationPrincipal AuthUserDetails authUser) {
        if (authUser == null) {
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

    @GetMapping("/products/{id}")
    public String showProductDetails(@PathVariable Integer id, Model model, Principal principal) {
        Product product = productsRepository.findById(id).orElseThrow();

        String createdByName = null;
        Optional<User> creator = Optional.empty();
        if (product.getCreatedBy() != null) {
            creator = userRepository.findById(product.getCreatedBy());
            if (creator.isPresent()) {
                createdByName = creator.get().getFirstName() + " " + creator.get().getLastName();
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("createdByFullName", createdByName);
        model.addAttribute("canEdit", principal != null
                && creator.isPresent()
                && creator.get().getEmail().equals(principal.getName()));

        if (principal != null && creator.isPresent() && creator.get().getEmail().equals(principal.getName())) {
            model.addAttribute("canEdit", true);
        }

        return "products/ShowProductDetails";
    }

}
