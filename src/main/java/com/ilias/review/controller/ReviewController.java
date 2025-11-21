package com.ilias.review.controller;

import com.ilias.review.mapper.ReviewMapper;
import com.ilias.review.model.dto.ReviewRequestDto;
import com.ilias.review.model.dto.ReviewResponseDto;
import com.ilias.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto addReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        return reviewMapper.toDto(reviewService.addReview(reviewRequestDto));
    }
}
