package com.androidhuman.rxfirebase2.database.model;

import android.support.annotation.NonNull;

public final class Some<T> extends DataValue<T> {

    private final T value;

    public Some(T value) {
        this.value = value;
    }

    @Override
    @NonNull
    public T get() {
        return value;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Some)) {
            return false;
        }

        Some<?> some = (Some<?>) o;

        return value.equals(some.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Some{"
                + "value=" + value
                + '}';
    }
}
