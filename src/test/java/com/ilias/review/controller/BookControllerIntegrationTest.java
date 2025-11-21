package com.ilias.review.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.ilias.review.model.dto.BookDetailsResponseDto;
import com.ilias.review.model.dto.BookSearchResponseDto;
import com.ilias.review.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8089)
@ActiveProfiles("test")
class BookControllerIntegrationTest {
    @Autowired
    private TestRestTemplate rest;

    @Test
    void searchBooks_shouldReturnMappedResults() {
        stubFor(get(urlPathEqualTo("/books"))
                .withQueryParam("search", equalTo("harry"))
                .willReturn(okJson(JsonUtils.readJson("api/book/search_harry.json"))));

        ResponseEntity<BookSearchResponseDto[]> response =
                rest.getForEntity("/api/v1/books?search=harry", BookSearchResponseDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].title()).isEqualTo("Harry Potter");
    }

    @Test
    void searchBooks_shouldReturnEmptyList_whenNoResultsInGutendex() {
        stubFor(get(urlPathEqualTo("/books"))
                .withQueryParam("search", equalTo("unknown"))
                .willReturn(okJson(JsonUtils.readJson("api/book/search_book_empty.json"))));

        ResponseEntity<BookSearchResponseDto[]> response =
                rest.getForEntity("/api/v1/books?search=unknown", BookSearchResponseDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getBookDetails_shouldReturnMappedBook() {
        stubFor(get(urlPathEqualTo("/books/200"))
                .willReturn(okJson(JsonUtils.readJson("api/book/book_valid_1.json"))));


        ResponseEntity<BookDetailsResponseDto> response =
                rest.getForEntity("/api/v1/books/200", BookDetailsResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BookDetailsResponseDto dto = response.getBody();
        assertThat(dto.id()).isEqualTo(200L);
        assertThat(dto.authors()).hasSize(1);
        assertThat(dto.title()).isEqualTo("Lord of the Rings");
    }

    @Test
    void getBookDetails_shouldReturn404_whenBookNotFoundInGutendex() {
        stubFor(get(urlPathEqualTo("/books/404"))
                .willReturn(aResponse().withStatus(404)));

        ResponseEntity<String> response =
                rest.getForEntity("/api/v1/books/404", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).contains("External API returned HTTP 404");
    }

    @Test
    void getBookDetails_shouldReturn502OnExternalServerError() {
        stubFor(get(urlPathEqualTo("/books/500"))
                .willReturn(aResponse().withStatus(500)));

        ResponseEntity<String> response =
                rest.getForEntity("/api/v1/books/500", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).contains("External API returned HTTP 500");
    }

    @Test
    void getBookDetails_shouldReturn502_whenInvalidJsonFromGutendex() {
        stubFor(get(urlPathEqualTo("/books/999"))
                .willReturn(okJson(JsonUtils.readJson("api/book/book_invalid.json"))));

        ResponseEntity<String> response =
                rest.getForEntity("/api/v1/books/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
    }
}