package com.bookstore.backend.orders;

import com.bookstore.backend.books.Book;
import com.bookstore.backend.books.BookService;
import com.bookstore.backend.core.exception.CustomNoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookService bookService;
    
    public Page<OrderResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::fromOrder);
    }
    
    @Transactional(rollbackFor = {CustomNoResultException.class,Exception.class})
    public OrderResponse create(OrderRequest orderRequest,String userId) {
        List<OrderDetail> list = new LinkedList<>();
        Order order = orderRepository.save(Order.formOrderRequest(orderRequest));
        order.setUserId(userId);
        orderRequest.getItems().forEach(orderDetailRequest -> {
            Book book = bookService.findByIsbn(orderDetailRequest.getIsbn());
            System.out.println("heheheheehehehehhehe"+  book.getAuthor());
            OrderDetail orderDetail = OrderDetail.fromOrderDetailRequest(orderDetailRequest);
            orderDetail.setOrder(order);
            orderDetail.setBook(book);
            orderDetail.setPrice(book.getPrice());
            list.add(orderDetail);
        });
        order.setOrderDetails(orderDetailRepository.saveAll(list));
        return OrderResponse.fromOrder(order);
    }
    
    @Transactional(rollbackFor = {CustomNoResultException.class,Exception.class})
    public OrderResponse update(String id , OrderRequest orderRequest) {
        Order order = orderRepository.findById(UUID.fromString(id)).orElseThrow(() -> new CustomNoResultException(Order.class ,"Not found order id " +id));
        order.setLastModifiedDate(Instant.now());
        order.setStatus(orderRequest.getStatus());
        return OrderResponse.fromOrder(order);
    }
    
    public OrderResponse findById(String id) {
        Order order = orderRepository.findById(UUID.fromString(id)).orElseThrow(() -> new CustomNoResultException(Order.class, "Not found order id " + id));
        return OrderResponse.fromOrder(order);
    }
    
    @Transactional(rollbackFor = {CustomNoResultException.class,Exception.class})
    public OrderResponse remove(String id) {
        Order order = orderRepository.findById(UUID.fromString(id)).orElseThrow(() -> new CustomNoResultException(Order.class ,"Not found order id " +id));
        order.setStatus(OrderStatus.CANCELLED);
        return  OrderResponse.fromOrder(orderRepository.save(order));
    }
    
    @Transactional(rollbackFor = {CustomNoResultException.class,Exception.class})
    public OrderResponse updateStatus(String id , OrderRequest orderRequest) {
        Order order = orderRepository.findById(UUID.fromString(id)).orElseThrow(() -> new CustomNoResultException(Order.class ,"Not found order id " +id));
        order.setLastModifiedDate(Instant.now());
        order.setStatus(orderRequest.getStatus());
        return OrderResponse.fromOrder(order);
    }
}
