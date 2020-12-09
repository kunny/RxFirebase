package com.androidhuman.rxfirebase3.database;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@AutoValue
public abstract class ChildMoveEvent extends ChildEvent {

    @CheckResult
    @NonNull
    public static ChildMoveEvent create(DataSnapshot dataSnapshot, String previousChildName) {
        return new AutoValue_ChildMoveEvent(dataSnapshot, previousChildName);
    }

    @Nullable
    public abstract String previousChildName();
}
