package com.bblets.baibuy.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.models.ProductDto;

import com.bblets.baibuy.models.User;
import com.bblets.baibuy.repository.ProductsRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository productsRepository;

    @GetMapping({ "", "/" })
    public String showProductList(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null ||
                !(loggedInUser.getRole() == User.Role.ADMIN || loggedInUser.getRole() == User.Role.SELLER)) {
            return "redirect:/auth/signin";
        }

        List<Product> products = productsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {

        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("producDto", "imageFile", "Image is required."));
        }

        if (result.hasErrors()) {
            return "products/CreateProduct";
        }

        // Save image file to server
        MultipartFile imageFile = productDto.getImageFile();
        Date createdAt = new Date(System.currentTimeMillis());
        String imageFileName = createdAt.getTime() + "_" + imageFile.getOriginalFilename();

        try {
            String uploadDir = "public/uploads/products/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + imageFileName), StandardCopyOption.REPLACE_EXISTING);

            }

        } catch (Exception error) {
            System.out.println("Exception: " + error.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(imageFileName);

        productsRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable int id, Model model) {

        try {
            Product product = productsRepository.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto", productDto);
        } catch (Exception error) {
            System.out.println("Exception: " + error.getMessage());
            return "redirect:/products";
        }

        return "products/EditProduct";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable int id, Model model, @Valid @ModelAttribute ProductDto productDto,
            BindingResult result) {

        try {
            Product product = productsRepository.findById(id).get();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/EditProduct";
            }

            if (!productDto.getImageFile().isEmpty()) {
                // Delete old image file
                String uploadDir = "public/uploads/products/";
                Path oldImageFilePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldImageFilePath);
                } catch (Exception error) {
                    System.out.println("Exception: " + error.getMessage());
                }

                // Save new image file to server
                MultipartFile imageFile = productDto.getImageFile();
                Date createdAt = new Date(System.currentTimeMillis());
                String imageFileName = createdAt.getTime() + "_" + imageFile.getOriginalFilename();

                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + imageFileName), StandardCopyOption.REPLACE_EXISTING);

                }

                product.setImageFileName(imageFileName);
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            productsRepository.save(product);
        } catch (Exception error) {
            System.out.println("Exception: " + error.getMessage());
            return "redirect:/products";
        }

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {

        try {
            Product product = productsRepository.findById(id).get();

            // Delete image file
            String uploadDir = "public/uploads/products/";
            Path imageFilePath = Paths.get(uploadDir + product.getImageFileName());

            try {
                Files.delete(imageFilePath);
            } catch (Exception error) {
                System.out.println("Exception: " + error.getMessage());
            }

            productsRepository.delete(product);
        } catch (Exception error) {
            System.out.println("Exception: " + error.getMessage());
            return "redirect:/products";
        }

        return "redirect:/products";
    }

}
