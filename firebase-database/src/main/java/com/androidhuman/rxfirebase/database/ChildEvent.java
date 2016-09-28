package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;

import android.support.annotation.NonNull;

public abstract class ChildEvent {

    final DataSnapshot dataSnapshot;

    ChildEvent(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    @NonNull
    public DataSnapshot dataSnapshot() {
        return dataSnapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChildEvent that = (ChildEvent) o;

        return dataSnapshot.equals(that.dataSnapshot);

    }

    @Override
    public int hashCode() {
        return dataSnapshot.hashCode();
    }
}
