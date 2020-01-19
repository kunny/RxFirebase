package com.androidhuman.rxfirebase2.database.model;

import com.google.auto.value.AutoValue;

import androidx.annotation.NonNull;

@AutoValue
public abstract class Empty<T> extends DataValue<T> {

    @Override
    @NonNull
    public T value() {
        throw new IllegalStateException("No value");
    }
}
