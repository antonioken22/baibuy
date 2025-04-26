package com.bblets.baibuy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.bblets.baibuy.security.AuthUserDetails;
import com.bblets.baibuy.services.CebuLocationService;
import com.bblets.baibuy.services.ReviewService;
import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.models.Report;
import com.bblets.baibuy.models.Report.ReportStatus;
import com.bblets.baibuy.models.ReviewDto;
import com.bblets.baibuy.models.TransactionRecord;
import com.bblets.baibuy.models.UserDto;
import com.bblets.baibuy.models.User;
import com.bblets.baibuy.repository.ProductsRepository;
import com.bblets.baibuy.repository.ReportRepository;
import com.bblets.baibuy.repository.ReviewRepository;
import com.bblets.baibuy.repository.TransactionRecordRepository;
import com.bblets.baibuy.repository.UserRepository;
import com.bblets.baibuy.repository.MessageRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    private MessageRepository messageRepository;    //wyups

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/")
    public String landingPage() {
        return "landing/Landing"; // This loads the landing page template directly at root
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

        Map<Integer, Double> productAvgRatings = new HashMap<>();
        for (Product product : otherProducts) {
            Double avgRating = reviewRepository.findAverageRatingByProductId(product.getId());
            productAvgRatings.put(product.getId(), avgRating != null ? avgRating : 0.0);
        }

        Map<Integer, Long> soldCounts = transactionRecordRepository.findAll().stream()
                .filter(tr -> tr.getConfirmedAt() != null)
                .collect(Collectors.groupingBy(
                        tr -> tr.getProduct().getId(),
                        Collectors.counting()));

        List<Product> topSellingProducts = otherProducts.stream()
                .filter(product -> soldCounts.getOrDefault(product.getId(), 0L) >= 10)
                .collect(Collectors.toList());

        model.addAttribute("topSellingProducts", topSellingProducts);
        model.addAttribute("soldCounts", soldCounts);
        model.addAttribute("products", otherProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("productMunicipalities", productMunicipalities);
        model.addAttribute("productAvgRatings", productAvgRatings);

        return "dashboard/UserDashboard";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        model.addAttribute("user", loggedInUser);

        return "Profile/profile";
    }

    @GetMapping("/products/{id}")
    public String showProductDetails(@PathVariable Integer id,
            Model model,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        Product product = productsRepository.findById(id).orElseThrow();

        // Creator name
        Optional<User> creatorOpt = userRepository.findById(product.getCreatedBy());
        String createdByName = creatorOpt
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .orElse("Unknown");

        String municipalityName = cebuLocationService.getMunicipalityByBarangay(product.getBarangayName());

        model.addAttribute("product", product);
        model.addAttribute("createdByFullName", createdByName);
        model.addAttribute("municipalityName", municipalityName);

        // Check if current user is the owner
        boolean canEdit = authUser != null && creatorOpt.isPresent() &&
                creatorOpt.get().getId().equals(authUser.getId());

        model.addAttribute("canEdit", canEdit);

        // Load reviews
        var reviews = reviewService.getReviewsByProduct(id);
        double avgRating = reviews.stream().mapToInt(r -> r.getRating()).average().orElse(0.0);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);

        // Review eligibility (only buyers who haven't reviewed yet)
        boolean canLeaveReview = false;
        Integer reviewedUserId = null;

        if (authUser != null) {
            var currentUser = userRepository.findById(authUser.getId()).orElseThrow();
            var recordOpt = transactionRecordRepository.findByBuyerAndProduct(currentUser, product);

            if (recordOpt.isPresent()) {
                TransactionRecord record = recordOpt.get();
                boolean isDelivered = record.getConfirmedAt() != null;
                boolean notYetReviewed = !record.isBuyerReviewed();
                if (isDelivered && notYetReviewed) {
                    canLeaveReview = true;
                    reviewedUserId = product.getCreatedBy();
                }
                model.addAttribute("hasBought", true);
            } else {
                model.addAttribute("hasBought", false);
            }
        }

        model.addAttribute("canLeaveReview", canLeaveReview);
        ReviewDto dto = new ReviewDto();
        dto.setProductId(product.getId());
        dto.setReviewedUserId(reviewedUserId); // If reviewedUserId is null, it’s okay — user can't submit.
        model.addAttribute("reviewDto", dto);
        model.addAttribute("reviewedUserId", reviewedUserId);

        if (canEdit) {
            Set<User> messageSenders = messageRepository.findDistinctSendersByProductId(id);
            model.addAttribute("messageSenders", messageSenders);
            model.addAttribute("isSeller", true);
        } else {
            model.addAttribute("isSeller", false);
        }

        return "products/ShowProductDetails";
    }

    @PostMapping("/products/buy/{productId}")
    public String realBuy(@PathVariable Integer productId, @AuthenticationPrincipal AuthUserDetails authUser) {
        if (authUser == null)
            return "redirect:/auth/signin";

        Optional<Product> productOpt = productsRepository.findById(productId);
        Optional<User> buyerOpt = userRepository.findById(authUser.getId());

        if (productOpt.isPresent() && buyerOpt.isPresent()) {
            Product product = productOpt.get();
            User buyer = buyerOpt.get();

            if (!buyer.getId().equals(product.getCreatedBy())) {
                var recordOpt = transactionRecordRepository.findByBuyerAndProduct(buyer, product);
                if (recordOpt.isEmpty()) {
                    TransactionRecord record = new TransactionRecord();
                    record.setProduct(product);
                    record.setBuyer(buyer);
                    record.setBuyerReviewed(false);
                    record.setSellerReviewed(false);

                    // Set seller from product
                    Optional<User> sellerOpt = userRepository.findById(product.getCreatedBy());
                    sellerOpt.ifPresent(record::setSeller);

                    // Do not set confirmedAt yet — wait until seller marks as delivered
                    transactionRecordRepository.save(record);
                }
            }
        }

        return "redirect:/products/" + productId;
    }

    @GetMapping("/seller/orders")
    public String sellerOrders(Model model, @AuthenticationPrincipal AuthUserDetails authUser) {
        if (authUser == null)
            return "redirect:/auth/signin";
        List<TransactionRecord> orders = transactionRecordRepository.findBySellerId(authUser.getId());
        model.addAttribute("orders", orders);
        return "products/SellerOrders";
    }

    @PostMapping("/seller/confirm-delivery/{transactionId}")
    public String confirmDelivery(@PathVariable Integer transactionId) {
        TransactionRecord record = transactionRecordRepository.findById(transactionId).orElseThrow();
        record.setConfirmedAt(LocalDateTime.now());
        transactionRecordRepository.save(record);
        return "redirect:/seller/orders";
    }

    @PostMapping("/seller/rate-buyer")
    public String rateBuyer(@RequestParam Integer transactionId,
            @RequestParam Integer rating) {

        TransactionRecord record = transactionRecordRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction ID"));

        if (record.getSellerReviewed() != null && record.getSellerReviewed()) {
            return "redirect:/seller/orders";
        }
        record.setSellerReviewed(true);
        transactionRecordRepository.save(record);

        return "redirect:/seller/orders";
    }

    @PostMapping("/report-user")
    public String reportUser(@RequestParam("reportedUserId") Integer reportedUserId,
            @RequestParam("reason") String reason,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        if (authUser == null) {
            return "redirect:/auth/signin";
        }

        User reporter = userRepository.findById(authUser.getId()).orElse(null);
        User reportedUser = userRepository.findById(reportedUserId).orElse(null);

        if (reporter == null || reportedUser == null) {
            return "redirect:/error";
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(reason);
        report.setStatus(ReportStatus.PENDING);
        report.setCreatedAt(LocalDateTime.now());

        reportRepository.save(report);

        return "redirect:/dashboard";
    }
}
