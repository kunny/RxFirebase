package com.androidhuman.firebase.auth.model;

import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

final class NonNullFirebaseUser extends OptionalFirebaseUser {

    @NonNull
    private final FirebaseUser user;

    NonNullFirebaseUser(@NonNull FirebaseUser user) {
        this.user = user;
    }

    @Override
    public FirebaseUser get() {
        return user;
    }

    @Override
    public boolean isPresent() {
        return true;
    }
}
