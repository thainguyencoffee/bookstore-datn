package com.bookstore.backend.vouchers.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class VoucherUpdateRequest {
    private Integer quantity;
    private Instant endDate;
    private Integer status;
}
