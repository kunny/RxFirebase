package com.androidhuman.rxfirebase3.database.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Some<T> extends DataValue<T> {

    @NonNull
    public abstract T value();
}
