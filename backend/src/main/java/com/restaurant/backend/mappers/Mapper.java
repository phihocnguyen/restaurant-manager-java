package com.restaurant.backend.mappers;

public interface Mapper<A, B> {
    B mapFrom(A a);
    A mapTo(B b);
}
