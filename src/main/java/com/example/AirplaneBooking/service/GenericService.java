package com.example.AirplaneBooking.service;

import java.util.List;
import java.util.UUID;

public interface GenericService<T, D> {
    D create(D dto);

    D update(UUID id, D dto);

    void delete(UUID id);

    D findById(UUID id);

    List<D> findAll();
}