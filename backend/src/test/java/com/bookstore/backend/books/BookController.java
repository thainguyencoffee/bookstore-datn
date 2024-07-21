package com.bookstore.backend.books;

import com.bookstore.backend.IntegrationTestsBase;
import com.bookstore.backend.books.dto.BookCreateDto;
import com.bookstore.backend.books.dto.BookUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

@Transactional
class BookController extends IntegrationTestsBase {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookService bookService;


    @Autowired
    private BookRepository bookRepository;

    @Test
    void whenGetAllBookAndAuthenticatedThen200() {
        webTestClient.get()
                .uri("/api/books")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenGetBookByIsbnThen200(){
        String queryParamValue = "1234567893";
        webTestClient.get()
                .uri("/api/books/{isbn}" ,queryParamValue)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenGetBookByIsbnFalse404(){
        webTestClient.get()
                .uri("/api/books/{isbn}", 999249999)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenGetAllBooksAndUnauthenticatedThen401() {
        webTestClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenGetBookByIsbnAndUnauthenticatedThen401() {
        webTestClient.get()
                .uri("/api/books/{isbn}", 1234567893)
                .exchange()
                .expectStatus().isUnauthorized();
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
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(book))
                .exchange()
                .expectStatus().isCreated();
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
    void whenDeleteBookThen200() {
        String queryParamValue = "1234567893";
        webTestClient.delete()
                .uri("/api/books/{isbn}" ,queryParamValue)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void whenDeleteBookWithUserNullThen401(){
        webTestClient.delete()
                .uri("/api/books/{isbn}" ,"1234567893")
                .headers(httpHeaders -> httpHeaders.setBearerAuth("meoconthichditamnang"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenDeleteBookWithUserNullThen401_2(){
        webTestClient.delete()
                .uri("/api/books/{isbn}", "1234567893")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenDeleteBookWithCustomerThen403() {
        webTestClient.delete()
                .uri("/api/books/{isbn}", "9783161484102")
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
