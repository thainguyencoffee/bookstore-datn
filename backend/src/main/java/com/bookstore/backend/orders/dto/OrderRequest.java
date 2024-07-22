package com.bookstore.backend.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {
    @NotBlank(message = "UserId can not be blank")
    private String userId;

    @NotBlank(message = "status can not be blank")
    private String status ;

    @Size(min = 1 , message = "Book quantity must be greater than 0")
    @Valid
    private List<OrderDetailRequest> items;
}
