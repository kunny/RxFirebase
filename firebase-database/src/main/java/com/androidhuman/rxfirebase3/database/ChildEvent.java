package com.androidhuman.rxfirebase3.database;

import com.google.firebase.database.DataSnapshot;

import android.support.annotation.NonNull;

public abstract class ChildEvent {

    ChildEvent() {

    }

    @NonNull
    public abstract DataSnapshot dataSnapshot();
}
