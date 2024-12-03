package com.example.isdfarmersmarket.business.exception.custom_exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EntityNotFoundException extends RuntimeException {
    private final Long id;
    private final Class<?> aClass;

    public EntityNotFoundException(Long id,
                                   Class<?> aClass) {
        super();
        this.id = id;
        this.aClass = aClass;
    }
}
