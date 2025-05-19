package kr.co.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(@NotBlank @Email String email,
                            @NotBlank String nickname,
                            @NotBlank String password) {
}
