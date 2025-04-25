package com.bblets.baibuy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.bblets.baibuy.security.AuthUserDetails;
import com.bblets.baibuy.services.CebuLocationService;
import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.models.UserDto;
import com.bblets.baibuy.models.User;
import com.bblets.baibuy.repository.ProductsRepository;
import com.bblets.baibuy.repository.UserRepository;
import com.bblets.baibuy.repository.MessageRepository;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Optional;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CebuLocationService cebuLocationService;

    @Autowired
    private MessageRepository messageRepository;

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

        List<Product> allProducts = productsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<Product> otherProducts = allProducts.stream()
                .filter(product -> !product.getCreatedBy().equals(authUser.getId()) &&
                        Boolean.TRUE.equals(product.getIsListed()) &&
                        !Boolean.TRUE.equals(product.isBlocked()))
                .collect(Collectors.toList());

        Set<String> categories = otherProducts.stream()
                .map(Product::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<String, List<String>> groupedBarangays = cebuLocationService.getAllCebuLocations();
        var productMunicipalities = otherProducts.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        p -> cebuLocationService.getMunicipalityByBarangay(p.getBarangayName())));

        model.addAttribute("products", otherProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("productMunicipalities", productMunicipalities);

        return "dashboard/UserDashboard";
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal AuthUserDetails authUser, Model model) {
        if (authUser == null) {
            return "redirect:/auth/signin";
        }

        User user = userRepository.findByEmail(authUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        model.addAttribute("user", user);
        return "profile/prof";
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

        String municipalityName = cebuLocationService.getMunicipalityByBarangay(product.getBarangayName());
        model.addAttribute("product", product);
        model.addAttribute("createdByFullName", createdByName);
        model.addAttribute("municipalityName", municipalityName);

        boolean canEdit = principal != null && creator.isPresent() &&
                creator.get().getEmail().equals(principal.getName());
        model.addAttribute("canEdit", canEdit);

        if (canEdit) {
            Set<User> messageSenders = messageRepository.findDistinctSendersByProductId(id);
            model.addAttribute("messageSenders", messageSenders);
            model.addAttribute("isSeller", true);
        } else {
            model.addAttribute("isSeller", false);
        }

        return "products/ShowProductDetails";
    }

    @GetMapping("/profile/address")
    public String showAddressPage(@AuthenticationPrincipal AuthUserDetails authUser, Model model) {
        if (authUser == null) {
            return "redirect:/auth/signin";
        }
        
        User user = userRepository.findByEmail(authUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        
        return "profile/address";
    }

    @GetMapping("/profile/notif")
    public String showNotifPage(@AuthenticationPrincipal AuthUserDetails authUser, Model model) {
        if (authUser == null) {
            return "redirect:/auth/signin";
        }
        
        User user = userRepository.findByEmail(authUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        
        return "profile/notif";
    }

    @GetMapping("/profile/privacy")
    public String showPrivacyPage(@AuthenticationPrincipal AuthUserDetails authUser, Model model) {
        if (authUser == null) {
            return "redirect:/auth/signin";
        }
        
        User user = userRepository.findByEmail(authUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        
        return "profile/privacy";
    }

    @GetMapping("/profile/password")
    public String showPasswordPage(@AuthenticationPrincipal AuthUserDetails authUser, Model model) {
        if (authUser == null) {
            return "redirect:/auth/signin";
        }
        
        User user = userRepository.findByEmail(authUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        
        return "profile/password";
    }
}