package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;


public final class RxFirebaseAuth {

    @CheckResult
    @NonNull
    public static Observable<FirebaseAuth> authStateChanges(@NonNull FirebaseAuth instance) {
        return new AuthStateChangesObservable(instance);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> createUserWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return new CreateUserWithEmailAndPasswordObservable(instance, email, password);
    }

    @CheckResult
    @NonNull
    public static Maybe<List<String>> fetchProvidersForEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return new FetchProvidersForEmailObservable(instance, email);
    }

    @CheckResult
    @NonNull
    public static Maybe<FirebaseUser> getCurrentUser(
            @NonNull final FirebaseAuth instance) {
        return new GetCurrentUserObservable(instance);
    }

    @CheckResult
    @NonNull
    public static Completable sendPasswordResetEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return new SendPasswordResetEmailObservable(instance, email);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInAnonymously(
            @NonNull FirebaseAuth instance) {
        return new SignInAnonymouslyObservable(instance);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithCredential(
            @NonNull FirebaseAuth instance, @NonNull AuthCredential credential) {
        return new SignInWithCredentialObservable(instance, credential);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithCustomToken(
            @NonNull FirebaseAuth instance, @NonNull String token) {
        return new SignInWithCustomTokenObservable(instance, token);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return new SignInWithEmailAndPasswordObservable(instance, email, password);
    }

    @CheckResult
    @NonNull
    public static Completable signOut(@NonNull FirebaseAuth instance) {
        return new SignOutObserver(instance);
    }

    private RxFirebaseAuth() {
        throw new AssertionError("No instances");
    }
}
