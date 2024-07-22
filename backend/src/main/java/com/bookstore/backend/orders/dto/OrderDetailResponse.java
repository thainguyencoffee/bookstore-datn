package com.bookstore.backend.orders;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private Long id;

    private String isbn;

    private Long price;

    private Integer quantity;

    private Long total;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .isbn(orderDetail.getBook().getIsbn())
                .total(orderDetail.getQuantity()*orderDetail.getPrice())
                .build();
    }

}
