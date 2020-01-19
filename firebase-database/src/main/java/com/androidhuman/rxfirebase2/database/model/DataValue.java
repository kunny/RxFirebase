package com.androidhuman.rxfirebase2.database.model;

import java.io.Serializable;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

public abstract class DataValue<T> implements Serializable {

    private static Empty<?> empty;

    @CheckResult
    @NonNull
    public static <T> DataValue<T> of(T value) {
        if (null != value) {
            return new AutoValue_Some<>(value);
        } else {
            return empty();
        }
    }

    @CheckResult
    @NonNull
    public static <T> DataValue<T> empty() {
        if (null == empty) {
            empty = new AutoValue_Empty<>();
        }
        //noinspection unchecked
        return (DataValue<T>) empty;
    }

    DataValue() {

    }

    @NonNull
    public abstract T value();
}
