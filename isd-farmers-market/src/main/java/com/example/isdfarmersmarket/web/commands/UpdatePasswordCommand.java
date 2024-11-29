package com.example.isdfarmersmarket.web.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdatePasswordCommand {
    private Long id;

    @NotBlank(message = "Old password cannot be blank")
    @Schema(example = "oldPassword123")
    private String oldPassword;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Schema(example = "NewPassword123!")
    private String password;
}
