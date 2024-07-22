package com.bookstore.backend.core.exception;

import jakarta.persistence.NoResultException;
import lombok.Getter;

@Getter
public class CustomNoResultException extends NoResultException {

    private final Class<?> clazz;

    public CustomNoResultException(final Class<?> clazz, Object id) {
        super("No result found for " + clazz.getSimpleName() + " with id " + id);
        this.clazz = clazz;
    }
}
