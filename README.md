# Book Review API

**Book Review API** is a simple Java Spring Boot application that allows users to:

1. Search for books by title using the **Gutendex API**.
2. Add reviews and ratings (0-5) for specific books.
3. Retrieve detailed information about a book, including reviews and average rating.


---

## Technologies

* Java 25
* Spring Boot 3.5.x
* Spring Data JPA + H2 (in-memory database)
* MapStruct (DTO mapping)
* WireMock (integration tests)
* Spring Validation
* Maven

---

## Project Structure

* **client/** – REST client for Gutendex API.
* **config/** – application configuration and external properties.
* **controller/** – REST controllers (`BookController`, `ReviewController`) and global `AdviceController` for exception handling.
* **mapper/** – MapStruct mappers for converting Gutendex and entity data to DTOs.
* **model/** – DTOs, JPA entities, and domain records.
* **repository/** – Spring Data JPA repository for reviews.
* **service/** – business logic for books and reviews.
* **util/** – helper for reading JSON files in tests.

---

## Running Locally

1. **Clone the repository**:

```bash
git clone <repo-url>
cd book-review-api
```

2. **Start the application**:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

3. **H2 Console** (in-memory database):

* URL: `http://localhost:8080/h2-console`
* JDBC URL: `jdbc:h2:mem:booksdb`
* User: `sa`
* Password: (leave empty)

---

## Configuration

Defined in `application.yaml`:

* Gutendex API base URL (`external.gutendex.base-url`)
* H2 database and JPA settings

For tests, `application-test.yaml`

---

## API Endpoints

### Search Books

```
GET /api/v1/books?search={title}
```

**Response:** List of `BookSearchResponseDto` objects:

* id, title, authors, languages, download count

---

### Get Book Details

```
GET /api/v1/books/{id}
```

**Response:** `BookDetailsResponseDto`

* id, title, authors, languages, download count
* average rating (`averageRating`)
* list of reviews (`reviews`)

---

### Add a Review

```
POST /api/v1/reviews
```

**Body:** `ReviewRequestDto`

```json
{
  "bookId": 1,
  "rating": 5,
  "content": "Great book!"
}
```

**Response:** `ReviewResponseDto` with review ID, date, and details.

> **Validation:**
>
> * `bookId` must be positive.
> * `rating` between 0–5.
> * `content` must not be blank.

---

## Testing

* **Unit tests:** `src/test/java/com/ilias/review/service`
* **Integration tests:** `src/test/java/com/ilias/review/controller/BookControllerIntegrationTest.java`
* WireMock simulates Gutendex API responses for integration tests.

Run tests:

```bash
mvn test
```

---

## Additional Information

* MapStruct automatically maps DTOs from Gutendex API to REST responses.
* Global `AdviceController` handles validation errors, malformed JSON, and external API errors.
* No authentication is required – the API is public.
