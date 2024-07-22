package com.bookstore.backend.orders.dto;

import com.bookstore.backend.orders.Order;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private UUID id;
    private String userId;
    private String status;
    private Instant createdDate;
    private List<OrderDetailResponse> items;

    public static OrderResponse fromOrder(Order order){
        return OrderResponse.builder()
                .userId(order.getUserId())
                .status(order.getStatus())
                .items(order.getOrderDetails().stream().map(OrderDetailResponse::fromOrderDetail).toList())
                .build();
    }
}
