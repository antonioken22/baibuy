package com.bblets.baibuy.models;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class ReviewDto {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 1000)
    private String comment;

    @NotNull
    private Integer reviewedUserId;

    @NotNull
    private Integer productId;
}
