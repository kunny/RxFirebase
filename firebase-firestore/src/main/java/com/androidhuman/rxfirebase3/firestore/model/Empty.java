package com.androidhuman.rxfirebase3.firestore.model;

import com.google.auto.value.AutoValue;

import android.support.annotation.NonNull;

@AutoValue
public abstract class Empty<T> extends Value<T> {

    @Override
    @NonNull
    public T value() {
        throw new IllegalStateException("No value");
    }
}
