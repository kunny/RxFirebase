package com.androidhuman.rxfirebase3.firestore.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Some<T> extends Value<T> {

    @NonNull
    public abstract T value();
}
