package com.androidhuman.rxfirebase2.firestore.model;

import com.google.auto.value.AutoValue;

import android.support.annotation.NonNull;

@AutoValue
public abstract class Some<T> extends Value<T> {

    @NonNull
    public abstract T value();
}
