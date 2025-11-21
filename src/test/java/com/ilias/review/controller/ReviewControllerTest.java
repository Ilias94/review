package com.ilias.review.controller;

import com.ilias.review.mapper.ReviewMapper;
import com.ilias.review.model.dao.Review;
import com.ilias.review.model.dto.ReviewResponseDto;
import com.ilias.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private ReviewMapper reviewMapper;

    @Test
    void addReview_shouldValidateBadInput() throws Exception {
        String invalidJson = """
                { "bookId": null, "rating": 10, "content": "" }
                """;

        mvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addReview_shouldReturnCreated() throws Exception {

        Review saved = new Review(1L, 10L, 5, "Nice!", null);
        ReviewResponseDto dto =
                new ReviewResponseDto(1L, 10L, 5, "Nice!", null);

        Mockito.when(reviewService.addReview(Mockito.any())).thenReturn(saved);
        Mockito.when(reviewMapper.toDto(saved)).thenReturn(dto);

        String validJson = """
                { "bookId": 10, "rating": 5, "content": "Nice!" }
                """;

        mvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.content").value("Nice!"));
    }
}