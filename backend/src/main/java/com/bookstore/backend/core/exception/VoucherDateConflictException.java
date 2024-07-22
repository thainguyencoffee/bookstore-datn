package com.bookstore.backend.core.exception;

import java.time.Instant;

public class VoucherDateConflictException extends RuntimeException {

    public VoucherDateConflictException(Instant startDate, Instant endDate) {
        super("Start date must be before end date. Start date: " + startDate + " End date: " + endDate);
    }

}
