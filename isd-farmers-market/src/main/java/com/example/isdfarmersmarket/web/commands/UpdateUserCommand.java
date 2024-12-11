package com.example.isdfarmersmarket.web.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserCommand {
    private Long id;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(example = "example@example.com")
    private String email;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Schema(example = "Mark")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Schema(example = "Brown")
    private String lastName;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10,}", message = "Phone number must contain at least 10 digits")
    @Schema(example = "1234567890")
    private String phoneNumber;

    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    @Schema(example = "123 Main St, Springfield")
    private String address;

    @Size(max = 255, message = "Description must be less than 255 characters")
    @Schema(example = "Optional description")
    private String description;
}
