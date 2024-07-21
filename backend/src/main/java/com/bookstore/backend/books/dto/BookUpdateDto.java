package com.bookstore.backend.books.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateDto {

    private String title;
    private String author;
    @Min(value = 0,message = "Price is not negative number")
    private Long price;

}
