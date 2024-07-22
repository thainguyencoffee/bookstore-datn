package com.bookstore.backend.orders;

import com.bookstore.backend.books.Book;
import com.bookstore.backend.orders.dto.OrderDetailRequest;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    private Long price;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    public static OrderDetail fromOrderDetailRequest(OrderDetailRequest orderDetailRequest){
        return  OrderDetail.builder()
                .quantity(orderDetailRequest.getQuantity())
                .build();
    }
}
