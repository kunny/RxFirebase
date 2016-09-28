package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class ChildMoveEvent extends ChildEvent {

    private final String previousChildName;

    @CheckResult
    @NonNull
    public static ChildMoveEvent create(DataSnapshot dataSnapshot, String previousChildName) {
        return new ChildMoveEvent(dataSnapshot, previousChildName);
    }

    private ChildMoveEvent(DataSnapshot dataSnapshot, String previousChildName) {
        super(dataSnapshot);
        this.previousChildName = previousChildName;
    }

    @Nullable
    public String previousChildName() {
        return previousChildName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChildMoveEvent that = (ChildMoveEvent) o;

        //noinspection SimplifiableIfStatement
        if (!dataSnapshot.equals(that.dataSnapshot)) {
            return false;
        }

        return previousChildName != null
                ? previousChildName.equals(that.previousChildName)
                : that.previousChildName == null;
    }

    @Override
    public int hashCode() {
        int result = dataSnapshot.hashCode();
        result = 31 * result + (previousChildName != null ? previousChildName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChildMoveEvent{"
                + "dataSnapshot=" + dataSnapshot
                + ", previousChildName='" + previousChildName + '\''
                + '}';
    }
}
