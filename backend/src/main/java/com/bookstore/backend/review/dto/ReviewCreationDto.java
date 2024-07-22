package com.bookstore.backend.review.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreationDto {
    @NotNull(message = "The rate of review must not be null")
    @Min(value = 1, message = "Min rate is 1")
    @Max(value = 5, message = "Max rate is 5")
    private Integer rate;
    @NotBlank(message = "Comment is required")
    @Size(min = 5, max = 255, message = "Max comment length is 255, min is 5")
    private String comment;

    public ReviewCreationDto(Integer rate, String comment) {
        this.rate = rate;
        this.comment = comment;
    }
}
