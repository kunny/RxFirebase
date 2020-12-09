package com.androidhuman.rxfirebase3.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

public abstract class ChildEvent {

    ChildEvent() {

    }

    @NonNull
    public abstract DataSnapshot dataSnapshot();
}
