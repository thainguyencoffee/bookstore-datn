package com.bookstore.backend.books.dto;

import com.bookstore.backend.books.Book;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateDto{

    @NotBlank
    @Size(min = 10, max = 13, message = "The ISBN must be between 10 and 13 characters long")
    @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "The ISBN must be valid")
    private String isbn;
    @NotBlank(message = "The title must be not blank")
    private String title;
    @NotBlank(message = "The author must be not blank")
    private String author;
    @NotNull(message = "The price must be not null")
    @Min(value = 1000, message = "The price of book must be greater than 1000 VND")
    private Long price;
    public static Book toEntity(BookCreateDto dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        return book;
    }
}
