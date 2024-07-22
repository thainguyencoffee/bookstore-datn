package com.bookstore.backend.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateDto {
    @Min(value = 1, message = "Min rate is 1")
    @Max(value = 5, message = "Max rate is 5")
    private Integer rate;
    @Size(min = 5, max = 255, message = "Max comment length is 255, min is 5")
    private String comment;
}
