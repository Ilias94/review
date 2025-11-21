package com.ilias.review.service;

import com.ilias.review.model.dao.Review;
import com.ilias.review.model.dto.ReviewRequestDto;
import com.ilias.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review addReview(ReviewRequestDto reviewRequest) {
        Review review = Review.builder()
                .bookId(reviewRequest.bookId())
                .rating(reviewRequest.rating())
                .content(reviewRequest.content())
                .createdDate(LocalDateTime.now())
                .build();

        return reviewRepository.save(review);
    }
}
