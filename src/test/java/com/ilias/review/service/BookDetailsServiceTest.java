package com.ilias.review.service;

import com.ilias.review.client.GutendexClient;
import com.ilias.review.config.GutendexConfig;
import com.ilias.review.mapper.GutendexBookMapper;
import com.ilias.review.mapper.ReviewMapper;
import com.ilias.review.model.dao.Review;
import com.ilias.review.model.dto.*;
import com.ilias.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookDetailsServiceTest {
    @InjectMocks
    private BookDetailsService bookDetailsService;

    @Mock
    private GutendexBookMapper gutendexBookMapper;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private GutendexConfig gutendexConfig;

    @Mock
    private GutendexClient gutendexClient;

    private GutendexRawBookDto rawBookDto;
    private BookDetailsResponseDto mappedDto;
    private Review review;
    private ReviewResponseDto reviewResponse;

    @BeforeEach
    void setup() {
        rawBookDto = new GutendexRawBookDto(
                1L,
                "Test Book",
                List.of(new GutendexAuthorDto("John Doe", 1970, null)),
                List.of("en"),
                10
        );

        mappedDto = BookDetailsResponseDto.builder()
                .id(1L)
                .title("Test Book")
                .authors(List.of(new AuthorResponseDto("John Doe", 1970, null)))
                .languages(List.of("en"))
                .downloadCount(10)
                .averageRating(null) // będzie nadpisane
                .reviews(null)       // będzie nadpisane
                .build();

        review = Review.builder()
                .id(100L)
                .bookId(1L)
                .rating(5)
                .content("Great!")
                .createdDate(LocalDateTime.now())
                .build();

        reviewResponse = new ReviewResponseDto(
                100L,
                1L,
                5,
                "Great!",
                review.getCreatedDate()
        );

        when(gutendexConfig.getBaseUrl()).thenReturn("http://fake");
    }

    @Test
    void shouldReturnMergedResult_WhenRawBookExists_AndHasReviews() {
        // given
        when(gutendexClient.getBook(any())).thenReturn(rawBookDto);
        when(gutendexBookMapper.toBookDetailsDto(rawBookDto)).thenReturn(mappedDto);
        when(reviewRepository.findByBookId(1L)).thenReturn(List.of(review));
        when(reviewMapper.toDtoList(List.of(review))).thenReturn(List.of(reviewResponse));
        when(reviewRepository.findAverageRatingByBookId(1L)).thenReturn(Optional.of(5.0));

        // when
        BookDetailsResponseDto result = bookDetailsService.getBookDetails(1L);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.authors()).hasSize(1);
        assertThat(result.reviews()).hasSize(1);
        assertThat(result.averageRating()).isEqualTo(5.0);
    }

    @Test
    void shouldReturnBookWithoutReviews_WhenNoReviewsExist() {
        when(gutendexClient.getBook(any())).thenReturn(rawBookDto);
        when(gutendexBookMapper.toBookDetailsDto(rawBookDto)).thenReturn(mappedDto);
        when(reviewRepository.findByBookId(1L)).thenReturn(List.of());
        when(reviewMapper.toDtoList(List.of())).thenReturn(List.of());
        when(reviewRepository.findAverageRatingByBookId(1L)).thenReturn(Optional.empty());

        BookDetailsResponseDto result = bookDetailsService.getBookDetails(1L);

        assertThat(result.reviews()).isEmpty();
        assertThat(result.averageRating()).isNull();
    }

    @Test
    void shouldReturnFallbackDto_WhenRawBookIsNull() {
        when(gutendexClient.getBook(any())).thenReturn(null);

        BookDetailsResponseDto result = bookDetailsService.getBookDetails(999L);

        assertThat(result.id()).isEqualTo(999L);
        assertThat(result.authors()).isEmpty();
        assertThat(result.languages()).isEmpty();
        assertThat(result.downloadCount()).isZero();
        assertThat(result.reviews()).isEmpty();
    }
}