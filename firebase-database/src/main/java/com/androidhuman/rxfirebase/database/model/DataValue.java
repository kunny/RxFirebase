package com.androidhuman.rxfirebase.database.model;

import java.io.Serializable;

public abstract class DataValue<T> implements Serializable {

    private static Empty<?> empty;

    public abstract T get();

    public abstract boolean isPresent();

    public static <T> DataValue<T> of(T value) {
        if (null != value) {
            return new Some<>(value);
        } else {
            return empty();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> DataValue<T> empty() {
        if (null == empty) {
            empty = new Empty<>();
        }
        return (DataValue<T>) empty;
    }
}
