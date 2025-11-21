package com.ilias.review.model.dto;

import lombok.Builder;

@Builder
public record AuthorResponseDto(
        String name,
        Integer birthYear,
        Integer deathYear
) {
}
