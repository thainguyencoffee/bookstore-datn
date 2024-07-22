package com.bookstore.backend.books;

import com.bookstore.backend.books.dto.BookCreateDto;
import com.bookstore.backend.books.dto.BookUpdateDto;
import com.bookstore.backend.core.exception.CustomNoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() ->
                new CustomNoResultException(Book.class, String.format("Book with isbn %s not found in database", isbn)));
    }

    @Transactional
    public Book addBook(BookCreateDto book) {
        return bookRepository.save(BookCreateDto.toEntity(book));
    }

    @Transactional
    public Book updateBook(String isbn,BookUpdateDto bookDto) {

        Book book = findByIsbn(isbn);
        if (bookDto.getTitle()!=null){
            book.setTitle(bookDto.getTitle());
        }
        if (bookDto.getAuthor()!=null){
            book.setAuthor(bookDto.getAuthor());
        }
        if (bookDto.getPrice()!=null){
            book.setPrice(bookDto.getPrice());
        }
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(String isbn) {
        if (bookRepository.findByIsbn(isbn).isEmpty()) {
            throw new CustomNoResultException(Book.class, String.format("Book with isbn %s not found in database", isbn));
        }
        bookRepository.deleteByIsbn(isbn);
    }


}
