package com.bookstore.backend.orders;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailRequest {
    @NotBlank
    @Size(min = 10, max = 13, message = "The ISBN must be between 10 and 13 characters long")
    @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN must be valid")
    private String isbn;

    @Min(value = 1 , message = "Quantity must greater than 0")
    private Integer quantity;
}
