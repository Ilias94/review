package com.ilias.review.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GutendexRawBookDto(
        Long id,
        String title,
        List<GutendexAuthorDto> authors,
        List<String> languages,
        Integer downloadCount
) {
}
