package com.ilias.review.model.dto;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long id,
        Long bookId,
        int rating,
        String content,
        LocalDateTime createdDate
) {
}
