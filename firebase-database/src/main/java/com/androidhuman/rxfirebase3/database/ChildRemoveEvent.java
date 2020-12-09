package com.androidhuman.rxfirebase3.database;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

@AutoValue
public abstract class ChildRemoveEvent extends ChildEvent {

    @CheckResult
    @NonNull
    public static ChildRemoveEvent create(DataSnapshot dataSnapshot) {
        return new AutoValue_ChildRemoveEvent(dataSnapshot);
    }
}
