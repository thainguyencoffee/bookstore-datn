package com.bookstore.backend.vouchers.dto;

import com.bookstore.backend.vouchers.Voucher;
import com.bookstore.backend.core.validation.VoucherValidConstraint;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@VoucherValidConstraint
public class VoucherCreateDto {
    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private Voucher.Type type;

    @Min(value = 0, message = "Value must be greater than 0")
    private Long value;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Start date is required")
    private Instant startDate;

    @NotNull(message = "End date is required")
    private Instant endDate;

    public Voucher toEntity() {
        var v = new Voucher();
        v.setCode(this.getCode());
        v.setStartDate(this.getStartDate());
        v.setEndDate(this.getEndDate());
        v.setType(this.getType());
        v.setValue(this.getValue());
        v.setQuantity(this.getQuantity());
        return v;
    }

}
