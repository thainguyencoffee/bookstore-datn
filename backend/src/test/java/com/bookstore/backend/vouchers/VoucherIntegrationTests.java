package com.bookstore.backend.vouchers;


import com.bookstore.backend.IntegrationTestsBase;
import com.bookstore.backend.vouchers.dto.VoucherCreateDto;
import com.bookstore.backend.vouchers.dto.VoucherUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class VoucherIntegrationTests extends IntegrationTestsBase {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private VoucherRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void whenVoucherExistingAndGetAllVoucherThen200() {
        webTestClient.get()
                .uri("/api/vouchers")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenVoucherExistingAndGetVoucherByIdThen200() {
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(Instant.now());
        v.setEndDate(Instant.now());
        repository.save(v);
        webTestClient.get()
                .uri("/api/vouchers/{id}", v.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void whenVoucherNotExistingAndGetVoucherByIdThen404() {
        webTestClient.get()
                .uri("/api/vouchers/{id}", 2)
                .exchange()
                .expectStatus()
                .isNotFound();
    }


    @Test
    void whenAuthenticatedAndCreateNewVoucherValidThen201() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        VoucherCreateDto v = new VoucherCreateDto();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);

        webTestClient.post()
                .uri("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(v))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(v.getCode())
                .jsonPath("$.value").isEqualTo(v.getValue())
                .jsonPath("$.quantity").isEqualTo(v.getQuantity())
                .jsonPath("$.type").isEqualTo("PERCENT")
                .jsonPath("$.startDate").isEqualTo(v.getStartDate().toString())
                .jsonPath("$.endDate").isEqualTo(v.getEndDate().toString())
                .jsonPath("$.lastModifiedDate").exists()
                .jsonPath("$.createdDate").exists()
                .jsonPath("$.createdBy").exists()
                .jsonPath("$.lastModifiedBy").exists();
    }

    @Test
    void whenUnAuthenticatedAndCreateNewVoucherValidThen401() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        VoucherCreateDto v = new VoucherCreateDto();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);

        webTestClient.post()
                .uri("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(v))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedWithCustomerRoleAndCreateNewVoucherValidThen403() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        VoucherCreateDto v = new VoucherCreateDto();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);

        webTestClient.post()
                .uri("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(v))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenAuthenticatedAndCreateNewVoucherWithTimeInValidThen400() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.minus(15, ChronoUnit.DAYS);
        VoucherCreateDto v = new VoucherCreateDto();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);

        webTestClient.post()
                .uri("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(v))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAuthenticatedAndCreateNewVoucherWithValueInValidThen400() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.minus(15, ChronoUnit.DAYS);
        VoucherCreateDto v = new VoucherCreateDto();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(101L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);

        webTestClient.post()
                .uri("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(v))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAuthenticatedAndCreateNewVoucherWithValueInValid2Then400() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.minus(15, ChronoUnit.DAYS);
        VoucherCreateDto v = new VoucherCreateDto();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.MONEY);
        v.setValue(-1L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);

        webTestClient.post()
                .uri("/api/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(v))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAuthenticatedAndVoucherExistingUpdateVoucherByIdWithVoucherValidThenOK() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);
        repository.save(v);

        var vUpdate = new VoucherUpdateDto();
        vUpdate.setCode("VOUCHER_CODE_UPDATE");
        vUpdate.setValue(30L);

        webTestClient.patch()
                .uri("/api/vouchers/" + v.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(vUpdate))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(vUpdate.getCode())
                .jsonPath("$.value").isEqualTo(vUpdate.getValue())
                .jsonPath("$.lastModifiedDate").exists()
                .jsonPath("$.createdDate").exists()
                .jsonPath("$.createdBy").exists()
                .jsonPath("$.lastModifiedBy").exists();
    }

    @Test
    void whenAuthenticatedAndVoucherExistingUpdateVoucherByIdWithValueVoucherInValidThen400() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);
        repository.save(v);

        var vUpdate = new VoucherUpdateDto();
        vUpdate.setCode("VOUCHER_CODE_UPDATE");
        vUpdate.setValue(1000000L);

        webTestClient.patch()
                .uri("/api/vouchers/" + v.getType())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(vUpdate))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAuthenticatedAndVoucherExistingUpdateVoucherByIdWithVoucherQuantityInValidThen400() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);
        repository.save(v);

        var vUpdate = new VoucherUpdateDto();
        vUpdate.setCode("VOUCHER_CODE_UPDATE");
        vUpdate.setValue(100L);
        vUpdate.setQuantity(-1);

        webTestClient.patch()
                .uri("/api/vouchers/" + v.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .body(BodyInserters.fromValue(vUpdate))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAuthenticatedWithCustomerRoleAndVoucherExistingUpdateVoucherByIdWithVoucherValidThen403() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);
        repository.save(v);

        var vUpdate = new VoucherUpdateDto();
        vUpdate.setCode("VOUCHER_CODE_UPDATE");
        vUpdate.setValue(30L);

        webTestClient.patch()
                .uri("/api/vouchers/" + v.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .body(BodyInserters.fromValue(vUpdate))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenAuthenticatedWithEmployeeRoleAndVoucherExistingDeleteVoucherByIdThen204() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);
        repository.save(v);

        webTestClient.delete()
                .uri("/api/vouchers/" + v.getId())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void whenAuthenticatedWithEmployeeRoleAndVoucherNotExistingDeleteVoucherByIdThen404() {
        webTestClient.delete()
                .uri("/api/vouchers/" + 123456)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(employeeToken.getAccessToken()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenUnAuthenticatedAndVoucherExistingDeleteVoucherByIdThen401() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);
        repository.save(v);

        webTestClient.delete()
                .uri("/api/vouchers/" + v.getId())
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenUnAuthenticatedAndVoucherNotExistingDeleteVoucherByIdThen401() {
        webTestClient.delete()
                .uri("/api/vouchers/" + 123456)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedWithCustomerRoleAndVoucherExistingDeleteVoucherByIdThen403() {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(15, ChronoUnit.DAYS);
        var v = new Voucher();
        v.setCode("VOUCHER_CODE");
        v.setQuantity(10);
        v.setType(Voucher.Type.PERCENT);
        v.setValue(20L);
        v.setStartDate(startDate);
        v.setEndDate(endDate);
        repository.save(v);

        webTestClient.delete()
                .uri("/api/vouchers/" + v.getId())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(customerToken.getAccessToken()))
                .exchange()
                .expectStatus().isForbidden();
    }

}
