package com.example.isdfarmersmarket.mappers;

public interface Mapper<E, D> {
    D toDto(E entity);
    E toEntity(D DTO);

    E toEntityNew(D DTO);
}
