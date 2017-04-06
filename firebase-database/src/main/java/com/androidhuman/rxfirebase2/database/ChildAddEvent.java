package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class ChildAddEvent extends ChildEvent {

    private final String previousChildName;

    @CheckResult
    @NonNull
    public static ChildAddEvent create(DataSnapshot dataSnapshot, String previousChildName) {
        return new ChildAddEvent(dataSnapshot, previousChildName);
    }

    private ChildAddEvent(DataSnapshot dataSnapshot, String previousChildName) {
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
        if (!super.equals(o)) {
            return false;
        }

        ChildAddEvent that = (ChildAddEvent) o;

        return previousChildName != null ? previousChildName.equals(that.previousChildName)
                : that.previousChildName == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + dataSnapshot.hashCode();
        result = 31 * result + (previousChildName != null ? previousChildName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChildAddEvent{"
                + "dataSnapshot=" + dataSnapshot
                + ", previousChildName='" + previousChildName + '\''
                + '}';
    }
}
