package com.androidhuman.firebase.auth.model;

import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.Nullable;

public abstract class OptionalFirebaseUser {

    public static OptionalFirebaseUser of(@Nullable FirebaseUser user) {
        if (null != user) {
            return new NonNullFirebaseUser(user);
        } else {
            return new EmptyFirebaseUser();
        }
    }

    OptionalFirebaseUser() {

    }

    public abstract FirebaseUser get();

    public abstract boolean isPresent();
}
