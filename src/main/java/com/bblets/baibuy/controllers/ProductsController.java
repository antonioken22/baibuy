package com.bblets.baibuy.controllers;

import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.models.Product.DeliveryPreference;
import com.bblets.baibuy.models.Product.ProductCondition;
import com.bblets.baibuy.models.ProductDto;
import com.bblets.baibuy.repository.ProductsRepository;
import com.bblets.baibuy.services.CebuLocationService;
import com.bblets.baibuy.services.FileStorageService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CebuLocationService locationService;
    @Autowired
    private AuditorAware<Integer> auditorAware;
    @Autowired
    private FileStorageService fileStorageService;

    // Product List (GET)
    @GetMapping({ "", "/" })
    public String showProductList(Model model) {
        List<Product> products = productsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    // Create Product (GET)
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        if (!model.containsAttribute("productDto")) {
            model.addAttribute("productDto", new ProductDto());
        }
        addLocationAndEnumAttributes(model);
        return "products/CreateProduct";
    }

    // Create Product (POST)
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("productDto") ProductDto productDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        validateImageFiles(productDto.getImageFiles(), bindingResult, true);

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors creating product: {}", bindingResult.getAllErrors());
            addLocationAndEnumAttributes(model);
            return "products/CreateProduct";
        }

        List<String> savedImageUrls;
        try {
            savedImageUrls = fileStorageService.storeFiles(productDto.getImageFiles());
            if (savedImageUrls.isEmpty()) {
                log.error("File storage returned empty list despite validation passed.");
                bindingResult.addError(new FieldError("productDto", "imageFiles", "Failed to save any images."));
                addLocationAndEnumAttributes(model);
                return "products/CreateProduct";
            }
        } catch (IOException e) {
            log.error("Error storing product images during create: {}", e.getMessage());
            model.addAttribute("errorMessage", "Could not save product images. Please try again.");
            addLocationAndEnumAttributes(model);
            return "products/CreateProduct";
        } catch (Exception e) {
            log.error("Unexpected error during image upload: {}", e.getMessage());
            model.addAttribute("errorMessage", "An unexpected error occurred during image upload.");
            addLocationAndEnumAttributes(model);
            return "products/CreateProduct";
        }

        try {
            Product product = new Product();
            mapDtoToEntity(productDto, product);
            product.setImageUrls(savedImageUrls);

            if (Boolean.TRUE.equals(product.getIsListed())) {
                product.setListedAt(LocalDateTime.now());
            } else {
                product.setListedAt(null);
            }

            productsRepository.save(product);
            log.info("Successfully created product with ID: {}", product.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
            return "redirect:/products";

        } catch (Exception e) {
            log.error("Error saving product entity: {}", e.getMessage());
            fileStorageService.deleteFiles(savedImageUrls);
            model.addAttribute("errorMessage", "Error saving product data to the database.");
            addLocationAndEnumAttributes(model);
            model.addAttribute("productDto", productDto);
            return "products/CreateProduct";
        }
    }

    // Edit Product (GET)
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = findProductById(id);

            if (!model.containsAttribute("productDto")) {
                ProductDto productDto = mapEntityToDto(product);
                model.addAttribute("productDto", productDto);
                model.addAttribute("currentMunicipality", productDto.getMunicipalityName());
                model.addAttribute("currentBarangay", productDto.getBarangayName());
            } else {
                ProductDto existingDto = (ProductDto) model.getAttribute("productDto");
                model.addAttribute("currentMunicipality", existingDto.getMunicipalityName());
                model.addAttribute("currentBarangay", existingDto.getBarangayName());
            }

            model.addAttribute("productId", id);
            model.addAttribute("existingImageUrls",
                    product.getImageUrls() != null ? product.getImageUrls() : Collections.emptyList());
            addLocationAndEnumAttributes(model);

            return "products/EditProduct";

        } catch (NoSuchElementException ex) {
            log.warn("Attempted to edit non-existent product with ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        } catch (Exception ex) {
            log.error("Error showing edit page for product ID {}: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving product details.");
            return "redirect:/products";
        }
    }

    // Edit Product (POST)
    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Integer id,
            @Valid @ModelAttribute("productDto") ProductDto productDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        Product product;
        try {
            product = findProductById(id);
        } catch (NoSuchElementException ex) {
            log.warn("Attempted to save edit for non-existent product ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        }

        List<String> originalImageUrls = product.getImageUrls() != null ? new ArrayList<>(product.getImageUrls())
                : new ArrayList<>();

        validateImageFiles(productDto.getImageFiles(), bindingResult, false);

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors editing product ID {}: {}", id, bindingResult.getAllErrors());
            model.addAttribute("productId", id);
            model.addAttribute("existingImageUrls", originalImageUrls);
            addLocationAndEnumAttributes(model);
            model.addAttribute("currentMunicipality", productDto.getMunicipalityName());
            model.addAttribute("currentBarangay", productDto.getBarangayName());
            return "products/EditProduct";
        }

        List<String> newImageUrls = null;
        boolean newFilesProvided = productDto.getImageFiles() != null &&
                productDto.getImageFiles().stream().anyMatch(f -> !f.isEmpty());

        if (newFilesProvided) {
            try {
                newImageUrls = fileStorageService.storeFiles(productDto.getImageFiles());
                if (newImageUrls.isEmpty() && productDto.getImageFiles().stream().anyMatch(f -> !f.isEmpty())) {
                    log.error("File storage returned empty list for new files during edit of product ID {}.", id);
                    model.addAttribute("errorMessage", "Failed to save the new product images. Please try again.");
                    model.addAttribute("productId", id);
                    model.addAttribute("existingImageUrls", originalImageUrls);
                    addLocationAndEnumAttributes(model);
                    model.addAttribute("currentMunicipality", productDto.getMunicipalityName());
                    model.addAttribute("currentBarangay", productDto.getBarangayName());
                    return "products/EditProduct";
                }
                log.info("Stored new images for product ID {}", id);
            } catch (IOException e) {
                log.error("Error storing new product images during edit for ID {}: {}", id, e.getMessage());
                model.addAttribute("errorMessage", "Could not save the new product images. Please try again.");
                model.addAttribute("productId", id);
                model.addAttribute("existingImageUrls", originalImageUrls);
                addLocationAndEnumAttributes(model);
                model.addAttribute("currentMunicipality", productDto.getMunicipalityName());
                model.addAttribute("currentBarangay", productDto.getBarangayName());
                return "products/EditProduct";
            } catch (Exception e) {
                log.error("Unexpected error during new image upload for ID {}: {}", id, e.getMessage());
                model.addAttribute("errorMessage", "An unexpected error occurred uploading new images.");
                model.addAttribute("productId", id);
                model.addAttribute("existingImageUrls", originalImageUrls);
                addLocationAndEnumAttributes(model);
                model.addAttribute("currentMunicipality", productDto.getMunicipalityName());
                model.addAttribute("currentBarangay", productDto.getBarangayName());
                return "products/EditProduct";
            }
        }

        mapDtoToEntity(productDto, product);

        List<String> urlsToDelete = new ArrayList<>();
        if (newFilesProvided && newImageUrls != null && !newImageUrls.isEmpty()) {
            product.setImageUrls(newImageUrls);
            urlsToDelete.addAll(originalImageUrls);
        } else {
            product.setImageUrls(originalImageUrls);
        }

        boolean currentlyListed = product.getListedAt() != null;
        boolean shouldBeListed = Boolean.TRUE.equals(product.getIsListed());

        if (shouldBeListed && !currentlyListed) {
            product.setListedAt(LocalDateTime.now());
        } else if (!shouldBeListed) {
            product.setListedAt(null);
        }

        try {
            productsRepository.save(product);
            log.info("Successfully updated product with ID: {}", id);

            if (!urlsToDelete.isEmpty()) {
                log.info("Attempting to delete old image files for product ID {}: {}", id, urlsToDelete);
                fileStorageService.deleteFiles(urlsToDelete);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
            return "redirect:/products";

        } catch (Exception e) {
            log.error("Error saving updated product entity for ID {}: {}", id, e.getMessage());

            if (newFilesProvided && newImageUrls != null && !newImageUrls.isEmpty()) {
                log.warn("Rolling back file changes for product ID {} due to DB error. Deleting new files: {}", id,
                        newImageUrls);
                fileStorageService.deleteFiles(newImageUrls);
            }

            model.addAttribute("errorMessage", "Error saving updated product data.");
            model.addAttribute("productId", id);
            model.addAttribute("existingImageUrls", originalImageUrls);
            addLocationAndEnumAttributes(model);
            model.addAttribute("currentMunicipality", productDto.getMunicipalityName());
            model.addAttribute("currentBarangay", productDto.getBarangayName());
            model.addAttribute("productDto", productDto);

            return "products/EditProduct";
        }
    }

    // Soft Delete Product (POST)
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Product product = findProductById(id);
            Integer auditorId = auditorAware.getCurrentAuditor().orElse(null);

            product.setDeletedAt(LocalDateTime.now());
            product.setDeletedBy(auditorId);

            productsRepository.save(product);
            log.info("Soft deleted product with ID: {} by auditor ID: {}", id, auditorId);

            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");

        } catch (NoSuchElementException ex) {
            log.warn("Attempted to delete non-existent product with ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error marking product ID {} as deleted: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product.");
        }
        return "redirect:/products";
    }

    // Helper Methods
    private Product findProductById(Integer id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + id));
    }

    private void addLocationAndEnumAttributes(Model model) {
        model.addAttribute("conditions", ProductCondition.values());
        model.addAttribute("deliveryPreferences", DeliveryPreference.values());
        if (!model.containsAttribute("municipalities")) {
            model.addAttribute("municipalities", locationService.getMunicipalities());
        }
    }

    private void validateImageFiles(List<MultipartFile> imageFiles, BindingResult bindingResult, boolean required) {
        boolean hasFiles = imageFiles != null && !imageFiles.isEmpty();
        boolean hasValidFile = hasFiles && imageFiles.stream().anyMatch(f -> !f.isEmpty());

        if (required && !hasValidFile) {
            bindingResult
                    .addError(new FieldError("productDto", "imageFiles", "At least one product image is required."));
            return;
        }
    }

    private void mapDtoToEntity(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setBrand(dto.getBrand());
        entity.setCategory(dto.getCategory());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setBarangayName(dto.getBarangayName());
        entity.setProductCondition(dto.getCondition());
        entity.setStocks(dto.getStocks());
        entity.setDeliveryPreference(dto.getDeliveryPreference());
        entity.setTags(dto.getTags() != null ? new ArrayList<>(dto.getTags()) : new ArrayList<>());
        entity.setIsListed(Boolean.TRUE.equals(dto.getIsListed()));
    }

    private ProductDto mapEntityToDto(Product entity) {
        ProductDto dto = new ProductDto();
        dto.setName(entity.getName());
        dto.setBrand(entity.getBrand());
        dto.setCategory(entity.getCategory());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        dto.setBarangayName(entity.getBarangayName());
        findMunicipalityForBarangay(entity.getBarangayName())
                .ifPresent(dto::setMunicipalityName);
        dto.setCondition(entity.getProductCondition());
        dto.setStocks(entity.getStocks());
        dto.setDeliveryPreference(entity.getDeliveryPreference());
        dto.setTags(entity.getTags() != null ? new ArrayList<>(entity.getTags()) : new ArrayList<>());
        dto.setIsListed(entity.getIsListed());
        return dto;
    }

    private Optional<String> findMunicipalityForBarangay(String barangayName) {
        if (barangayName == null || barangayName.isBlank()) {
            return Optional.empty();
        }
        Map<String, List<String>> allLocations = locationService.getAllCebuLocations();
        return allLocations.entrySet().stream()
                .filter(entry -> entry.getValue().contains(barangayName))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
