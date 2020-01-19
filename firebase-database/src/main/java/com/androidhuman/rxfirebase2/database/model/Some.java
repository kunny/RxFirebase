package com.androidhuman.rxfirebase2.database.model;

import com.google.auto.value.AutoValue;

import androidx.annotation.NonNull;

@AutoValue
public abstract class Some<T> extends DataValue<T> {

    @NonNull
    public abstract T value();
}
