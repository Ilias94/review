package com.ilias.review.model.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        int status,
        String error,
        String detail,
        LocalDateTime timestamp
) {
}
