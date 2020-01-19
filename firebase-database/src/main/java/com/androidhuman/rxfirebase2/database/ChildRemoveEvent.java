package com.androidhuman.rxfirebase2.database;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

@AutoValue
public abstract class ChildRemoveEvent extends ChildEvent {

    @CheckResult
    @NonNull
    public static ChildRemoveEvent create(DataSnapshot dataSnapshot) {
        return new AutoValue_ChildRemoveEvent(dataSnapshot);
    }
}
