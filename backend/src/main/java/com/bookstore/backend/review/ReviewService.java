package com.bookstore.backend.review;

import com.bookstore.backend.books.Book;
import com.bookstore.backend.books.BookService;
import com.bookstore.backend.core.exception.CustomNoResultException;
import com.bookstore.backend.review.dto.ReviewCreationDto;
import com.bookstore.backend.review.dto.ReviewUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final BookService bookService;
    private final ReviewRepository reviewRepository;

    public Review save(String isbn, ReviewCreationDto reqBody) {
        Book book = bookService.findByIsbn(isbn);
        Review review = new Review();
        review.setBook(book);
        review.setRate(reqBody.getRate());
        review.setComment(reqBody.getComment());
        return reviewRepository.save(review);
    }

    public Page<Review> findAllByBook(String isbn, Pageable pageable) {
        return reviewRepository.findAllByBook(bookService.findByIsbn(isbn), pageable);
    }

    public Review findById(String isbn, Long id) {
        bookService.findByIsbn(isbn);
        return reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Review not found with id: {}", id);
                    return new CustomNoResultException(Review.class, id);
                });
    }

    public void deleteById(String isbn, Long id) {
        reviewRepository.delete(findById(isbn, id));
    }

    public Review update(String isbn, Long id, ReviewUpdateDto reqBody) {
        Review review = findById(isbn, id);
        if (reqBody.getRate() != null) {
            review.setRate(reqBody.getRate());
        }
        if (reqBody.getComment() != null) {
            review.setComment(reqBody.getComment());
        }
        return reviewRepository.save(review);
    }

}
