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
    public String redirectToSignIn() {
        return "redirect:/auth/signin";
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
        public String userProfile(HttpSession session, Model model) {
     User loggedInUser = (User) session.getAttribute("loggedInUser");

      if (loggedInUser == null || loggedInUser.getRole() != User.Role.USER) {
         return "redirect:/auth/signin";
    }

     model.addAttribute("user", loggedInUser); // So the view can use it

        return "Profile/prof"; // This points to /templates/Profile/prof.html
    }
  @GetMapping("/profile/address")
    public String showAddressPage() {
        return "profile/address"; // No .html extension, and path is relative to templates/
    }
    


}
