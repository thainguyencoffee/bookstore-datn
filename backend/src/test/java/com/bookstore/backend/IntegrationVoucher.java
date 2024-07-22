package com.bookstore.backend;


import com.bookstore.backend.vouchers.Voucher;
import com.bookstore.backend.vouchers.VoucherRepository;
import com.bookstore.backend.vouchers.dto.VoucherRequest;
import com.bookstore.backend.vouchers.dto.VoucherUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Instant;

public class IntegrationVoucher extends IntegrationTestsBase {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private VoucherRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void whenGetAllVoucherAndAuthenticatedThen200() {
        webTestClient.get()
                .uri("/api/vouchers")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenGetVoucherByIdAndAuthenticatedThen200() {
        Voucher voucher = Voucher.builder()
                .code("VC001")
                .type(Voucher.Type.PERCENT)
                .value(20L)
                .quantity(10)
                .startDate(Instant.now())
                .endDate(Instant.now())
                .createdBy("")
                .createdDate(Instant.now())
                .lastModifiedBy("")
                .lastModifiedDate(Instant.now())
                .status(1)
                .build();
        Voucher v = repository.save(voucher);
        Long voucherId = v.getId();
        webTestClient.get()
                .uri("/api/vouchers/{id}", voucherId)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenGetVoucherByIdAndAuthenticatedFailedThen404() {
        Voucher voucher = Voucher.builder()
                .code("VC001")
                .type(Voucher.Type.PERCENT)
                .value(20L)
                .quantity(10)
                .startDate(Instant.now())
                .endDate(Instant.now())
                .createdBy("")
                .createdDate(Instant.now())
                .lastModifiedBy("")
                .lastModifiedDate(Instant.now())
                .status(1)
                .build();
        System.out.println("List voucher" + repository.findAll().size());
        webTestClient.get()
                .uri("/api/vouchers/{id}", 2)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenGetVoucherByCodeAndAuthenticatedThen200() {
        Voucher voucher = Voucher.builder()
                .code("VC001")
                .type(Voucher.Type.PERCENT)
                .value(20L)
                .quantity(10)
                .startDate(Instant.now())
                .endDate(Instant.now())
                .createdBy("")
                .createdDate(Instant.now())
                .lastModifiedBy("")
                .lastModifiedDate(Instant.now())
                .status(1)
                .build();
        Voucher v = repository.save(voucher);
        String code = v.getCode();
        System.out.println(v);
        System.out.println("List voucher" + repository.findAll().size());
        webTestClient.get()
                .uri("/api/vouchers/code/{code}", code)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenGetVoucherByCodeAndAuthenticatedFailedThen404() {
        Voucher voucher = Voucher.builder()
                .code("VC001")
                .type(Voucher.Type.PERCENT)
                .value(20L)
                .quantity(10)
                .startDate(Instant.now())
                .endDate(Instant.now())
                .createdBy("")
                .createdDate(Instant.now())
                .lastModifiedBy("")
                .lastModifiedDate(Instant.now())
                .status(1)
                .build();
        Voucher v = repository.save(voucher);
        System.out.println(v);
        System.out.println("List voucher" + repository.findAll().size());
        webTestClient.get()
                .uri("/api/vouchers/code/{code}", "VC002")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenGetAllVouchersAndUnauthenticatedThen200() {
        webTestClient
                .get()
                .uri("/api/vouchers")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenAddVoucherThen201() {
        Instant now = Instant.now();
        VoucherRequest voucher = new VoucherRequest();
        voucher.setCode("1234567999");
        voucher.setStatus(0);
        voucher.setQuantity(100);
        voucher.setStartdate(now);
        voucher.setEnddate(now);
        voucher.setValue(100L);
        voucher.setType(Voucher.Type.PERCENT);

        String startDateString = now.toString(); // Chuyển đổi thành định dạng ISO 8601
        String endDateString = now.toString();

        webTestClient.post()
                .uri("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(voucher))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(voucher.getCode())
                .jsonPath("$.status").isEqualTo(voucher.getStatus())
                .jsonPath("$.value").isEqualTo(voucher.getValue())
                .jsonPath("$.quantity").isEqualTo(voucher.getQuantity())
                .jsonPath("$.type").isEqualTo("PERCENT")
                .jsonPath("$.startDate").isEqualTo(startDateString)
                .jsonPath("$.endDate").isEqualTo(endDateString);
    }

    @Test
    void whenUpdateVoucherByIdAndAuthenticatedThenAccepted() {
        VoucherUpdateRequest request = new VoucherUpdateRequest();
        request.setQuantity(20);
        request.setEndDate(Instant.now().plusSeconds(3600));
        request.setStatus(1);

        Voucher voucher = Voucher.builder()
                .code("VC001")
                .type(Voucher.Type.PERCENT)
                .value(20L)
                .quantity(10)
                .startDate(Instant.now())
                .endDate(Instant.now().plusSeconds(3600))
                .createdBy("")
                .createdDate(Instant.now())
                .lastModifiedBy("")
                .lastModifiedDate(Instant.now())
                .status(0)
                .build();

        Voucher v = repository.save(voucher);

        webTestClient.patch()
                .uri("/api/vouchers/" + v.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isAccepted() // Kiểm tra trạng thái 202 ACCEPTED
                .expectBody()
                .jsonPath("$.id").isEqualTo(v.getId())
                .jsonPath("$.code").isEqualTo(v.getCode())
                .jsonPath("$.type").isEqualTo("PERCENT")
                .jsonPath("$.value").isEqualTo(v.getValue())
                .jsonPath("$.quantity").isEqualTo(request.getQuantity())
                .jsonPath("$.status").isEqualTo(request.getStatus());
    }

    @Test
    void whenUpdateVoucherByIdAndAuthenticatedFailedThen404() {
        VoucherUpdateRequest request = new VoucherUpdateRequest();
        request.setQuantity(20);
        request.setEndDate(Instant.now().plusSeconds(3600));
        request.setStatus(1);

        Voucher voucher = Voucher.builder()
                .code("VC001")
                .type(Voucher.Type.PERCENT)
                .value(20L)
                .quantity(10)
                .startDate(Instant.now())
                .endDate(Instant.now().plusSeconds(3600))
                .createdBy("")
                .createdDate(Instant.now())
                .lastModifiedBy("")
                .lastModifiedDate(Instant.now())
                .status(0)
                .build();

        Voucher v = repository.save(voucher);

        webTestClient.patch()
                .uri("/api/vouchers/" + 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isAccepted() // Kiểm tra trạng thái 202 ACCEPTED
                .expectBody()
                .jsonPath("$.id").isEqualTo(v.getId())
                .jsonPath("$.code").isEqualTo(v.getCode())
                .jsonPath("$.type").isEqualTo("PERCENT")
                .jsonPath("$.value").isEqualTo(v.getValue())
                .jsonPath("$.quantity").isEqualTo(request.getQuantity())
                .jsonPath("$.status").isEqualTo(request.getStatus());
    }

    @Test
    void whenDeleteVoucherByIdAndAuthenticatedThen200(){
        Voucher voucher = Voucher.builder()
                .code("VC001")
                .type(Voucher.Type.PERCENT)
                .value(20L)
                .quantity(10)
                .startDate(Instant.now())
                .endDate(Instant.now().plusSeconds(3600)) // Tăng thời gian kết thúc để tránh các vấn đề về thời gian
                .createdBy("testUser")
                .createdDate(Instant.now())
                .lastModifiedBy("testUser")
                .lastModifiedDate(Instant.now())
                .status(0)
                .build();

        Voucher v = repository.save(voucher);
        webTestClient
                .delete()
                .uri(String.format("/api/vouchers/%d", v.getId())) // Sử dụng ID được trả về từ việc lưu
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .exchange()
                .expectStatus()
                .isNoContent();
    }


}
