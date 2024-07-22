package com.bookstore.backend.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = VoucherValidConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VoucherValidConstraint {

    String message() default "This voucher is a percent type and must have a value between 1 and 100";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
