package com.example.retea_de_socializare.domain.validators;

import com.example.retea_de_socializare.exceptions.ValidationException;

@FunctionalInterface
public interface Validator<T> {

    void validate(T entity) throws ValidationException;
}