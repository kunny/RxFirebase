package com.androidhuman.rxfirebase2.database;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
