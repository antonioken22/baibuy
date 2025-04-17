package com.bblets.baibuy.models;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bblets.baibuy.models.Product.DeliveryPreference;
import com.bblets.baibuy.models.Product.ProductCondition;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    @Size(max = 10, message = "You can upload a maximum of 10 images.")
    private List<MultipartFile> imageFiles;

    @NotBlank(message = "Product name cannot be empty.")
    private String name;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.00", inclusive = true, message = "Price must be zero or positive.")
    private BigDecimal price;

    @NotBlank(message = "Barangay location cannot be empty.")
    private String barangayName;

    private String municipalityName;

    @NotBlank(message = "Product category cannot be empty.")
    private String category;

    @NotNull(message = "Product condition is required.")
    private ProductCondition condition;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 10, message = "Product description must be at least 10 characters long.")
    @Size(max = 2000, message = "Product description cannot be longer than 2000 characters.")
    private String description;

    @NotBlank(message = "Product brand cannot be empty.")
    private String brand;

    @NotNull(message = "Stock quantity is required.")
    @Min(value = 0, message = "Stocks cannot be negative.")
    private Integer stocks;

    @NotNull(message = "Delivery preference is required.")
    private DeliveryPreference deliveryPreference;

    private List<String> tags;

    @NotNull(message = "Please specify if the product is listed.")
    private Boolean isListed;
}