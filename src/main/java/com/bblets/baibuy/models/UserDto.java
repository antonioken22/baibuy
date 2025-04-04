package com.bblets.baibuy.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;

    @NotEmpty(message = "First name is required.")
    private String firstName;

    @NotEmpty(message = "Last name is required.")
    private String lastName;

    private String imageUrl;
}
