package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

public final class ChildRemoveEvent extends ChildEvent {

    @CheckResult
    @NonNull
    public static ChildRemoveEvent create(DataSnapshot dataSnapshot) {
        return new ChildRemoveEvent(dataSnapshot);
    }

    private ChildRemoveEvent(DataSnapshot dataSnapshot) {
        super(dataSnapshot);
    }

    @Override
    public String toString() {
        return "ChildRemoveEvent{"
                + "dataSnapshot=" + dataSnapshot
                + '}';
    }
}
