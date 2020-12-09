package com.androidhuman.rxfirebase3.database;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

@AutoValue
public abstract class ChildChangeEvent extends ChildEvent {

    @CheckResult
    @NonNull
    public static ChildChangeEvent create(DataSnapshot dataSnapshot, String previousChildName) {
        return new AutoValue_ChildChangeEvent(dataSnapshot, previousChildName);
    }

    @Nullable
    public abstract String previousChildName();
}
