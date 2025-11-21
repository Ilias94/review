package com.ilias.review.service;

import com.ilias.review.client.GutendexClient;
import com.ilias.review.config.GutendexConfig;
import com.ilias.review.mapper.GutendexBookMapper;
import com.ilias.review.model.dto.GutendexRawBookDto;
import com.ilias.review.model.domain.GutendexBooksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {
    private final GutendexBookMapper gutendexBookMapper;
    private final GutendexConfig gutendexConfig;
    private final GutendexClient gutendexClient;

    public List<GutendexRawBookDto> searchBooks(String query) {
        URI uri = UriComponentsBuilder
                .fromUriString(gutendexConfig.getBaseUrl())
                .path("/books")
                .queryParam("search", query)
                .build()
                .toUri();


        GutendexBooksResponse response = gutendexClient.getBooks(uri);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results();
    }
}
