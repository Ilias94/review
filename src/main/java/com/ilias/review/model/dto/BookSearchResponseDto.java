package com.ilias.review.model.dto;

import java.util.List;

public record BookSearchResponseDto(
        Long id,
        String title,
        List<AuthorResponseDto> authors,
        List<String> languages,
        Integer downloadCount
) {
}
