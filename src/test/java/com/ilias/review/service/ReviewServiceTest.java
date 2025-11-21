package com.ilias.review.service;

import com.ilias.review.model.dao.Review;
import com.ilias.review.model.dto.ReviewRequestDto;
import com.ilias.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Test
    void addReview_shouldSaveReviewWithCorrectData() {
        // given
        ReviewRequestDto request = new ReviewRequestDto(
                1L,
                5,
                "Amazing book!"
        );

        Review savedReview = Review.builder()
                .id(10L)
                .bookId(1L)
                .rating(5)
                .content("Amazing book!")
                .createdDate(LocalDateTime.now())
                .build();

        when(reviewRepository.save(any())).thenReturn(savedReview);

        // when
        Review result = reviewService.addReview(request);

        // then
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review captured = captor.getValue();

        assertThat(captured.getBookId()).isEqualTo(1L);
        assertThat(captured.getRating()).isEqualTo(5);
        assertThat(captured.getContent()).isEqualTo("Amazing book!");
        assertThat(captured.getCreatedDate()).isNotNull();

        assertThat(result).isEqualTo(savedReview);
    }

    @Test
    void addReview_shouldSetCreatedDate() {
        // given
        ReviewRequestDto request = new ReviewRequestDto(
                2L,
                4,
                "Good!"
        );

        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        Review result = reviewService.addReview(request);

        // then
        assertThat(result.getCreatedDate()).isNotNull();
        assertThat(result.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}