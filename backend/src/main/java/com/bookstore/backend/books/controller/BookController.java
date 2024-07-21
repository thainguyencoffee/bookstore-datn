package com.bookstore.backend.books.controller;

import com.bookstore.backend.books.Book;
import com.bookstore.backend.books.BookService;
import com.bookstore.backend.books.dto.BookCreateDto;
import com.bookstore.backend.books.dto.BookUpdateDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @GetMapping("/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public Book getBook(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@Valid @RequestBody BookCreateDto bookDto) {
         return bookService.addBook(bookDto);
    }


    @PatchMapping("/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@Validated @RequestBody BookUpdateDto bookDto,
                           @PathVariable String isbn) {
        return bookService.updateBook(isbn,bookDto);
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }

}
