package com.bookstore.backend.orders;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderResponse> getAll(Pageable page){
        return orderService.findAll(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse findById(@PathVariable("id") String id){
        return orderService.findById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse crateOrder(@RequestBody @Valid OrderRequest orderRequest, @AuthenticationPrincipal String userId){

        return orderService.create(orderRequest,userId);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse updateOrder(@PathVariable("id")String id
            , @RequestBody OrderRequest orderRequest ){

        return orderService.update(id , orderRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("id") String id){
         orderService.remove(id);
    }

}
