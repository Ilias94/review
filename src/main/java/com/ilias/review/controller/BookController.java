package com.ilias.review.controller;

import com.ilias.review.mapper.GutendexBookMapper;
import com.ilias.review.mapper.ReviewMapper;
import com.ilias.review.model.dto.BookDetailsResponseDto;
import com.ilias.review.model.dto.BookSearchResponseDto;
import com.ilias.review.model.dto.GutendexRawBookDto;
import com.ilias.review.service.BookDetailsService;
import com.ilias.review.service.BookSearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@Validated
public class BookController {
    private final BookSearchService bookSearchService;
    private final BookDetailsService bookDetailsService;
    private final GutendexBookMapper gutendexBookMapper;
    private final ReviewMapper reviewMapper;

    @GetMapping
    public List<BookSearchResponseDto> searchBooks(@RequestParam @NotBlank String search) {
        List<GutendexRawBookDto> rawList = bookSearchService.searchBooks(search);

        return gutendexBookMapper.fromExternalListToBookSearchDtoList(rawList);
    }

    @GetMapping("/{id}")
    public BookDetailsResponseDto getBook(@PathVariable @Positive Long id) {
        return bookDetailsService.getBookDetails(id);
    }
}
