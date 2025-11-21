package com.ilias.review.model.domain;

import com.ilias.review.model.dto.GutendexRawBookDto;

import java.util.List;

public record GutendexBooksResponse(
        int count,
        List<GutendexRawBookDto> results
) {
}
