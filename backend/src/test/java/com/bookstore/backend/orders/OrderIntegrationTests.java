package com.bookstore.backend.orders;

import com.bookstore.backend.IntegrationTestsBase;
import com.bookstore.backend.books.Book;
import com.bookstore.backend.books.BookRepository;
import com.bookstore.backend.orders.dto.OrderDetailRequest;
import com.bookstore.backend.orders.dto.OrderRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

public class OrderIntegrationTests extends IntegrationTestsBase {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        Book book = new Book();
        book.setIsbn("1234567891");
        book.setTitle("hehe book");
        book.setAuthor("nga dep trai");
        book.setPrice(5000L);
        bookRepository.save(book);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void whenGetAllOrdersThen200() {
        webTestClient.get()
                .uri("/api/orders")
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void whenGetAllOrdersAndUnAuthenticationThen403() {
        webTestClient.get()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenDeleteOrderThen204() {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setUserId("i");
        order.setId(UUID.randomUUID());
        order = orderRepository.save(order);
        webTestClient.delete()
                .uri("/api/orders/{id}", order.getId())
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void whenUpdateOrderThen200() {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setUserId("i");
        order.setId(UUID.randomUUID());
        order = orderRepository.save(order);
    OrderRequest orderRequest = createTestOrderRequest();
        webTestClient.patch()
                .uri("/api/orders/{id}", order.getId())
                .headers(headers -> headers.setBearerAuth(customerToken.getAccessToken()))
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(orderRequest.getStatus());
    }

    private OrderRequest createTestOrderRequest() {
        OrderDetailRequest orderDetailRequest = OrderDetailRequest.builder()
                .isbn("1234567891")
                .quantity(1)
                .build();
        return OrderRequest.builder()
                .userId("test-user")
                .status(OrderStatus.PENDING)
                .items(List.of(orderDetailRequest))
                .build();
    }
}
