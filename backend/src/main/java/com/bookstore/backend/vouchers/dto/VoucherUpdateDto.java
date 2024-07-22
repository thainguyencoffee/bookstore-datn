package com.bookstore.backend.vouchers.dto;

import com.bookstore.backend.vouchers.Voucher;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class VoucherUpdateDto {
    private String code;
    private Voucher.Type type;
    @Min(value = 0, message = "Value must be greater than 0")
    private Long value;
    @Min(value = 0, message = "Quantity must be greater than 0")
    private Integer quantity;
    private Instant startDate;
    private Instant endDate;
}
