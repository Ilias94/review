package com.ilias.review.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GutendexAuthorDto(
        String name,
        Integer birthYear,
        Integer deathYear
) {
}
