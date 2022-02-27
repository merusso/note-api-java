package com.example.noteapi.service;

public interface TwoWayConverter<A, B> {
    B convert(A a);
    A convertReverse(B b);
}
