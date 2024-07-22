package com.bookstore.backend.books.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateDto {

    private String title;
    private String author;
    @Min(value = 1000, message = "The price of book must be greater than 1000 VND")
    private Long price;

}
