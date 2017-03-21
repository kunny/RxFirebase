package com.androidhuman.rxfirebase2.database.model;

import java.util.NoSuchElementException;

public final class Empty<T> extends DataValue<T> {

    Empty() {

    }

    @Override
    public T get() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public String toString() {
        return "Empty DataValue";
    }
}
