package com.bblets.baibuy.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    @NotEmpty(message = "Product name cannot be empty.")
    private String name;

    @NotEmpty(message = "Product brand cannot be empty.")
    private String brand;

    @NotEmpty(message = "Product category cannot be empty.")
    private String category;

    @Min(0)
    private double price;

    @Size(min = 10, message = "Product description must be at least 10 characters long.")
    @Size(max = 1000, message = "Product description cannot be longer than 1000 characters.")
    private String description;

    private MultipartFile imageFile;
}
