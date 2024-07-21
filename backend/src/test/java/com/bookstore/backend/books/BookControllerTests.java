package com.bookstore.backend.books;

import com.bookstore.backend.IntegrationTestsBase;
import com.bookstore.backend.books.dto.BookCreateDto;
import com.bookstore.backend.books.dto.BookUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

class BookControllerTests extends IntegrationTestsBase {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void whenGetAllBookAndAuthenticatedThen200() {
        webTestClient.get()
                .uri("/api/books")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenGetBookByIsbnThen200() {
        Book book = new Book();
        book.setIsbn("1234567891");
        book.setTitle("Meo Meo");
        book.setPrice(5000L);
        book.setAuthor("Meo1");
        bookRepository.save(book);
        System.out.println(bookRepository.findAll().size());
        webTestClient.get()
                .uri("/api/books/{isbn}", "1234567891")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenGetBookByIsbnFalse404() {
        webTestClient.get()
                .uri("/api/books/{isbn}", 999249999)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenGetAllBooksAndUnauthenticatedThen200() {
        webTestClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void whenAddBookThen201() {
        BookCreateDto book = new BookCreateDto();
        book.setIsbn("1234567999");
        book.setTitle("New Book Title");
        book.setAuthor("New Author");
        book.setPrice(19999L);
        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(book))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo(book.getTitle())
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.author").isEqualTo(book.getAuthor())
                .jsonPath("$.price").isEqualTo(book.getPrice());
    }

    @Test
    void whenAddBookWithInvalidIsbnThen400() {
        BookCreateDto book = new BookCreateDto();
        book.setIsbn("meomeomeomeo");
        book.setTitle("New Book Title");
        book.setAuthor("New Author");
        book.setPrice(19999L);

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .bodyValue(book)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAddBookWithNullPriceThen400() {
        BookCreateDto book = new BookCreateDto();
        book.setIsbn("144871647985");
        book.setTitle("New Book Title");
        book.setAuthor("New Author");
        book.setPrice(null);

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .bodyValue(book)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAddBookWithoutRequiredFieldsThen400() {
        BookCreateDto book = new BookCreateDto();
        book.setIsbn("144871647985");
        book.setTitle(null);
        book.setAuthor("Some Author");
        book.setPrice(19999L);

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .bodyValue(book)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenUpdateBookThen404() {
        BookUpdateDto bookToUpdate = new BookUpdateDto();
        bookToUpdate.setTitle("New Book Title");
        webTestClient.patch()
                .uri("/api/books/{isbn}", "144871647985")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void whenBookExistingAndUpdateBookInValidThen400() {
        Book book = new Book();
        book.setIsbn("1234567891");
        book.setTitle("Meo Meo");
        book.setPrice(5000L);
        book.setAuthor("Meo1");
        bookRepository.save(book);
        BookUpdateDto bookToUpdate = new BookUpdateDto();
        bookToUpdate.setAuthor("Meo");
        bookToUpdate.setTitle("New Book Title");
        bookToUpdate.setPrice(900L);
        webTestClient.patch()
                .uri("/api/books/{isbn}", book.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenBookExistingAndUpdateBookValidThen200() {
        Book book = new Book();
        book.setIsbn("1234567891");
        book.setTitle("Meo Meo");
        book.setPrice(5000L);
        book.setAuthor("Meo1");
        bookRepository.save(book);
        BookUpdateDto bookToUpdate = new BookUpdateDto();
        bookToUpdate.setAuthor("Meo");
        bookToUpdate.setTitle("New Book Title");
        bookToUpdate.setPrice(20000000L);
        webTestClient.patch()
                .uri("/api/books/{isbn}", book.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo(bookToUpdate.getTitle())
                .jsonPath("$.author").isEqualTo(bookToUpdate.getAuthor())
                .jsonPath("$.price").isEqualTo(bookToUpdate.getPrice());
    }



    @Test
    void whenDeleteBookThen200() {
        Book book = new Book();
        book.setIsbn("1234567891");
        book.setTitle("Meo Meo");
        book.setPrice(5000L);
        book.setAuthor("Meo1");
        bookRepository.save(book);
        webTestClient.delete()
                .uri("/api/books/{isbn}", "1234567891")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenDeleteBookWithUserNullThen401() {
        webTestClient.delete()
                .uri("/api/books/{isbn}", "1234567893")
                .headers(httpHeaders -> httpHeaders.setBearerAuth("meoconthichditamnang"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenDeleteBookWithUserNullThen401_2() {
        webTestClient.delete()
                .uri("/api/books/{isbn}", "1234567893")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenDeleteBookWithCustomerThen403() {
        Book book = new Book();
        book.setIsbn("1234567891");
        book.setTitle("Meo Meo");
        book.setPrice(5000L);
        book.setAuthor("Meo1");
        bookRepository.save(book);
        webTestClient.delete()
                .uri("/api/books/{isbn}", "1234567891")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenDeleteBookNotWrongThen404() {
        webTestClient.delete()
                .uri("/api/books/{isbn}", "meovangthichancaran")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .exchange()
                .expectStatus().isNotFound();
    }

}
