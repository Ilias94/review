package com.ilias.review.service;

import com.ilias.review.client.GutendexClient;
import com.ilias.review.config.GutendexConfig;
import com.ilias.review.mapper.GutendexBookMapper;
import com.ilias.review.mapper.ReviewMapper;
import com.ilias.review.model.dto.BookDetailsResponseDto;
import com.ilias.review.model.dto.GutendexRawBookDto;
import com.ilias.review.model.dto.ReviewResponseDto;
import com.ilias.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDetailsService {
    private final GutendexBookMapper gutendexBookMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final GutendexConfig gutendexConfig;
    private final GutendexClient gutendexClient;

    public BookDetailsResponseDto getBookDetails(Long bookId) {

        URI uri = UriComponentsBuilder
                .fromUriString(gutendexConfig.getBaseUrl())
                .path("/books/")
                .path(bookId.toString())
                .build()
                .toUri();

        GutendexRawBookDto raw = gutendexClient.getBook(uri);

        if (raw == null) {
            return BookDetailsResponseDto.builder()
                    .id(bookId)
                    .authors(java.util.List.of())
                    .languages(java.util.List.of())
                    .downloadCount(0)
                    .reviews(java.util.List.of())
                    .build();
        }

        BookDetailsResponseDto base = gutendexBookMapper.toBookDetailsDto(raw);

        List<ReviewResponseDto> reviews = reviewMapper.toDtoList(
                reviewRepository.findByBookId(bookId)
        );

        Double avg = reviewRepository.findAverageRatingByBookId(bookId)
                .orElse(null);

        return BookDetailsResponseDto.builder()
                .id(base.id())
                .title(base.title())
                .authors(base.authors())
                .languages(base.languages())
                .downloadCount(base.downloadCount())
                .averageRating(avg)
                .reviews(reviews)
                .build();
    }
}

