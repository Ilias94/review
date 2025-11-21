package com.ilias.review.service;

import com.ilias.review.client.GutendexClient;
import com.ilias.review.config.GutendexConfig;
import com.ilias.review.model.domain.GutendexBooksResponse;
import com.ilias.review.model.dto.GutendexAuthorDto;
import com.ilias.review.model.dto.GutendexRawBookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookSearchServiceTest {
    @InjectMocks
    private BookSearchService bookSearchService;

    @Mock
    private GutendexConfig gutendexConfig;

    @Mock
    private GutendexClient gutendexClient;

    private GutendexRawBookDto raw1;
    private GutendexRawBookDto raw2;

    @BeforeEach
    void setup() {
        when(gutendexConfig.getBaseUrl()).thenReturn("http://fake");

        raw1 = new GutendexRawBookDto(
                1L,
                "Book One",
                List.of(new GutendexAuthorDto("Alice", 1980, null)),
                List.of("en"),
                100
        );

        raw2 = new GutendexRawBookDto(
                2L,
                "Book Two",
                List.of(new GutendexAuthorDto("Bob", 1975, null)),
                List.of("de"),
                50
        );
    }

    @Test
    void searchBooks_shouldReturnListOfBooks() {
        GutendexBooksResponse response = new GutendexBooksResponse(2, List.of(raw1, raw2));
        when(gutendexClient.getBooks(any())).thenReturn(response);

        List<GutendexRawBookDto> result = bookSearchService.searchBooks("test");

        assertThat(result).containsExactly(raw1, raw2);
    }

    @Test
    void searchBooks_shouldReturnEmptyList_WhenApiReturnsNull() {
        when(gutendexClient.getBooks(any())).thenReturn(null);

        List<GutendexRawBookDto> result = bookSearchService.searchBooks("whatever");

        assertThat(result).isEmpty();
    }

    @Test
    void searchBooks_shouldReturnEmptyList_WhenResultsAreNull() {
        GutendexBooksResponse response = new GutendexBooksResponse(10, null);
        when(gutendexClient.getBooks(any())).thenReturn(response);

        List<GutendexRawBookDto> result = bookSearchService.searchBooks("x");

        assertThat(result).isEmpty();
    }

    @Test
    void searchBooks_shouldCallApiWithCorrectUri() {
        GutendexBooksResponse response = new GutendexBooksResponse(0, List.of());
        String expected = "http://fake/books?search=harry%20potter";
        when(gutendexClient.getBooks(any())).thenReturn(response);

        bookSearchService.searchBooks("harry potter");

        ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
        verify(gutendexClient).getBooks(uriCaptor.capture());

        URI uri = uriCaptor.getValue();
        assertThat(uri).hasToString(expected);

    }
}