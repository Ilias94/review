package com.ilias.review.model.dto;

import jakarta.validation.constraints.*;

public record ReviewRequestDto(
        @NotNull @Positive Long bookId,
        @Min(0) @Max(5) int rating,
        @NotBlank String content
) {
}
