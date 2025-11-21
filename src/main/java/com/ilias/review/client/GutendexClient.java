package com.ilias.review.client;

import com.ilias.review.model.dto.GutendexRawBookDto;
import com.ilias.review.model.domain.GutendexBooksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class GutendexClient {
    private final RestTemplate restTemplate;

    public GutendexRawBookDto getBook(URI uri) {
        return restTemplate.getForObject(uri, GutendexRawBookDto.class);
    }

    public GutendexBooksResponse getBooks(URI uri) {
        return restTemplate.getForObject(uri, GutendexBooksResponse.class);
    }
}
