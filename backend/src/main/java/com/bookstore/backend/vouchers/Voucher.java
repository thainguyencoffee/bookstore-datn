package com.bookstore.backend.vouchers;

import com.bookstore.backend.vouchers.dto.VoucherRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "voucher")
@ToString
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Long value;
    private Integer quantity;
    @CreatedDate
    private Instant startDate;
    @CreatedDate
    private Instant endDate;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private Instant lastModifiedDate;
    private Integer status;
    @Version
    private int version;

    public enum Type{
        PERCENT,MONEY
    }

    public static Voucher toEntity(VoucherRequest request) {
        return Voucher.builder()
                .code(request.getCode())
                .type(Type.valueOf(request.getType().name()))
                .value(request.getValue())
                .startDate(request.getStartdate())
                .endDate(request.getEnddate())
                .createdBy(request.getCreatedby())
                .createdDate(request.getCreateddate())
                .lastModifiedBy(request.getLastmodifiedby())
                .lastModifiedDate(request.getLastmodifieddate())
                .status(request.getStatus())
                .build();
    }

}