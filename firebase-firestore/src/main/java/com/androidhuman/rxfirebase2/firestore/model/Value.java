package com.androidhuman.rxfirebase2.firestore.model;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.io.Serializable;

public abstract class Value<T> implements Serializable {

    private static Empty<?> empty;

    @CheckResult
    @NonNull
    public static <T> Value<T> of(T value) {
        if (null != value) {
            return new AutoValue_Some<>(value);
        } else {
            return empty();
        }
    }

    @CheckResult
    @NonNull
    public static <T> Value<T> empty() {
        if (null == empty) {
            empty = new AutoValue_Empty<>();
        }
        //noinspection unchecked
        return (Value<T>) empty;
    }

    Value() {

    }

    @NonNull
    public abstract T value();
}
