package com.bookstore.backend.reviews;

import com.bookstore.backend.IntegrationTestsBase;
import com.bookstore.backend.books.Book;
import com.bookstore.backend.books.BookRepository;
import com.bookstore.backend.review.Review;
import com.bookstore.backend.review.dto.ReviewCreationDto;
import com.bookstore.backend.review.dto.ReviewUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class ReviewIntegrationTests extends IntegrationTestsBase {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    WebTestClient webTestClient;

    @AfterEach
    void clearData() {
        bookRepository.deleteAll();
    }

    @Test
    void whenBookExistingGetPageOfReviewsReturn200() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);
        bookRepository.save(book);

        webTestClient.get().uri("/api/books/1234567890/reviews")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenBookAndReviewExistingGetPageOfReviewAndMatchesValue() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);

        Review review = new Review();
        review.setBook(book);
        review.setRate(4);
        review.setComment("Good");
        book.addReview(review);
        bookRepository.save(book);

        webTestClient.get().uri("/api/books/1234567890/reviews")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content[0].rate").isEqualTo(review.getRate())
                .jsonPath("$.content[0].comment").isEqualTo(review.getComment())
                .jsonPath("$.content[0].book.isbn").isEqualTo(book.getIsbn())
                .jsonPath("$.totalPages").isEqualTo(1)
                .jsonPath("$.totalElements").isEqualTo(1);
    }

    @Test
    void whenBookNotExistingGetPageOfReviewsReturn404() {
        webTestClient.get().uri("/api/books/1234567890/reviews")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenBookExistingCreateAReviewAndReturn201() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);
        bookRepository.save(book);

        var reviewDto = new ReviewCreationDto(3, "This is a good book");
        webTestClient
                .post().uri("/api/books/" + book.getIsbn() + "/reviews")
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(reviewDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.rate").isEqualTo(reviewDto.getRate())
                .jsonPath("$.comment").isEqualTo(reviewDto.getComment())
                .jsonPath("$.createdBy").isEqualTo("user");
    }

    @Test
    void whenBookExistingCreateAReviewInvalidReturn400() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);
        bookRepository.save(book);

        var reviewDto = new ReviewCreationDto(-1, "This is a good book");
        webTestClient
                .post().uri("/api/books/" + book.getIsbn() + "/reviews")
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(reviewDto))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenBookNotExistingCreateAReviewValidReturn404() {
        var reviewDto = new ReviewCreationDto(5, "This is a good book");
        webTestClient
                .post().uri("/api/books/1234567890/reviews")
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(reviewDto))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenBookAndReviewExistingDeleteReturn204() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);

        Review review = new Review();
        review.setBook(book);
        review.setRate(4);
        review.setComment("Good");
        book.addReview(review);
        bookRepository.save(book);

        webTestClient
                .delete().uri("/api/books/" + book.getIsbn() + "/reviews/" + review.getId())
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void whenBookExistingAndReviewNotExistingDeleteReturn404() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);
        bookRepository.save(book);

        webTestClient
                .delete().uri("/api/books/" + book.getIsbn() + "/reviews/" + "12345678")
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenBookAndReviewExistingUpdateReviewReturnOk() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);

        Review review = new Review();
        review.setBook(book);
        review.setRate(4);
        review.setComment("Good");
        book.addReview(review);
        bookRepository.save(book);

        var reviewUpdateDto = new ReviewUpdateDto(3, "This is updated comment");
        webTestClient.patch().uri("/api/books/" + book.getIsbn() + "/reviews/" + review.getId())
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(reviewUpdateDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.rate").isEqualTo(reviewUpdateDto.getRate())
                .jsonPath("$.comment").isEqualTo(reviewUpdateDto.getComment());
    }

    @Test
    void whenBookExistingAndReviewNotExistingUpdateReviewReturn404() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);
        bookRepository.save(book);

        var reviewIdNotExisting = 100L;
        var reviewUpdateDto = new ReviewUpdateDto(3, "This is updated comment");
        webTestClient.patch().uri("/api/books/" + book.getIsbn() + "/reviews/" + reviewIdNotExisting)
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(reviewUpdateDto))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenBookAndReviewExistingUpdateReviewInvalidReturn400() {
        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(120000000L);

        Review review = new Review();
        review.setBook(book);
        review.setRate(4);
        review.setComment("Good");
        book.addReview(review);
        bookRepository.save(book);

        var reviewUpdateDto = new ReviewUpdateDto(6, "This is updated comment");
        webTestClient.patch().uri("/api/books/" + book.getIsbn() + "/reviews/" + review.getId())
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(reviewUpdateDto))
                .exchange()
                .expectStatus().isBadRequest();
    }
}
