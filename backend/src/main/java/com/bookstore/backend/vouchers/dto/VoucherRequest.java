package com.bookstore.backend.vouchers.dto;

import com.bookstore.backend.vouchers.Voucher;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
public class VoucherRequest {
    private String code;
    @Enumerated(EnumType.STRING)
    private Voucher.Type type;
    private Long value;
    private Integer quantity;
    @CreatedDate
    private Instant startdate;
    @CreatedDate
    private Instant enddate;
    @CreatedBy
    private String createdby;
    @CreatedDate
    private Instant createddate;
    @LastModifiedBy
    private String lastmodifiedby;
    @LastModifiedDate
    private Instant lastmodifieddate;
    private Integer status;
}
