package com.androidhuman.rxfirebase3.database.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Empty<T> extends DataValue<T> {

    @Override
    @NonNull
    public T value() {
        throw new IllegalStateException("No value");
    }
}
