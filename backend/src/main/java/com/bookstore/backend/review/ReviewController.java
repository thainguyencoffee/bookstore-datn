package com.bookstore.backend.review;

import com.bookstore.backend.review.dto.ReviewCreationDto;
import com.bookstore.backend.review.dto.ReviewUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/api/books/{isbn}/reviews")
    public Page<Review> findAllByBook(@PathVariable String isbn, Pageable pageable) {
        return reviewService.findAllByBook(isbn, pageable);
    }

    @GetMapping("/api/books/{isbn}/reviews/{id}")
    public Review findById(@PathVariable String isbn, @PathVariable Long id) {
        return reviewService.findById(isbn, id);
    }

    @PostMapping("/api/books/{isbn}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Review create(@PathVariable String isbn, @Valid @RequestBody ReviewCreationDto reqBody, @AuthenticationPrincipal Jwt jwt) {
        var username = jwt.getClaim(StandardClaimNames.PREFERRED_USERNAME);
        log.info("{} attempt to create a review for book {}", username, isbn);
        return reviewService.save(isbn, reqBody);
    }

    @DeleteMapping("/api/books/{isbn}/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn, @PathVariable Long id) {
        reviewService.deleteById(isbn, id);
    }

    @PatchMapping("/api/books/{isbn}/reviews/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review update(@PathVariable String isbn, @PathVariable Long id, @Validated @RequestBody ReviewUpdateDto reqBody) {
        return reviewService.update(isbn, id, reqBody);
    }

}
