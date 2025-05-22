    package com.group.SpringMVCProject.dto;

    import jakarta.persistence.Column;
    import jakarta.validation.constraints.NotBlank;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class ResetPasswordRequestDto {
        public Long id;
        private String newToken;
        @NotBlank(message = "New password is required")
        private String newPassword;
    }
