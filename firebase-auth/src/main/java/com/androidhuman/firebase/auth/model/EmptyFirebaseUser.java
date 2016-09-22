package com.androidhuman.firebase.auth.model;

import com.google.firebase.auth.FirebaseUser;

import java.util.NoSuchElementException;

final class EmptyFirebaseUser extends OptionalFirebaseUser {

    @Override
    public FirebaseUser get() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}
