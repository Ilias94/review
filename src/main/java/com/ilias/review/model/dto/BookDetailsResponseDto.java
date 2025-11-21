package com.ilias.review.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BookDetailsResponseDto(
        Long id,
        String title,
        List<AuthorResponseDto> authors,
        List<String> languages,
        Integer downloadCount,
        Double averageRating,
        List<ReviewResponseDto> reviews
) {
}
