package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase.common.model.TaskResult;
import com.memoizrlabs.retrooptional.Optional;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

public final class RxFirebaseAuth {

    @CheckResult
    @NonNull
    public static Observable<FirebaseAuth> authStateChanges(@NonNull FirebaseAuth instance) {
        return Observable.create(new AuthStateChangesOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Observable<FirebaseUser> createUserWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return Observable.create(
                new CreateUserWithEmailAndPasswordOnSubscribe(instance, email, password));
    }

    @CheckResult
    @NonNull
    public static Observable<Optional<List<String>>> fetchProvidersForEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return Observable.create(new FetchProvidersForEmailOnSubscribe(instance, email));
    }

    @CheckResult
    @NonNull
    public static Observable<Optional<FirebaseUser>> getCurrentUser(
            @NonNull final FirebaseAuth instance) {
        return Observable.create(new GetCurrentUserOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> sendPasswordResetEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return Observable.create(new SendPasswordResetEmailOnSubscribe(instance, email));
    }

    @CheckResult
    @NonNull
    public static Observable<FirebaseUser> signInAnonymous(
            @NonNull FirebaseAuth instance) {
        return Observable.create(new SignInAnonymousOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Observable<FirebaseUser> signInWithCredential(
            @NonNull FirebaseAuth instance, @NonNull AuthCredential credential) {
        return Observable.create(new SignInWithCredentialOnSubscribe(instance, credential));
    }

    @CheckResult
    @NonNull
    public static Observable<FirebaseUser> signInWithCustomToken(
            @NonNull FirebaseAuth instance, @NonNull String token) {
        return Observable.create(new SignInWithCustomTokenOnSubscribe(instance, token));
    }

    @CheckResult
    @NonNull
    public static Observable<FirebaseUser> signInWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return Observable.create(
                new SignInWithEmailAndPasswordOnSubscribe(instance, email, password));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> signOut(@NonNull FirebaseAuth instance) {
        return Observable.create(new SignOutOnSubscribe(instance));
    }

    private RxFirebaseAuth() {
        throw new AssertionError("No instances");
    }
}
