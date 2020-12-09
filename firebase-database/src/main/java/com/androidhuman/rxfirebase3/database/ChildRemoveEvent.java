package com.androidhuman.rxfirebase3.database;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

@AutoValue
public abstract class ChildRemoveEvent extends ChildEvent {

    @CheckResult
    @NonNull
    public static ChildRemoveEvent create(DataSnapshot dataSnapshot) {
        return new AutoValue_ChildRemoveEvent(dataSnapshot);
    }
}
