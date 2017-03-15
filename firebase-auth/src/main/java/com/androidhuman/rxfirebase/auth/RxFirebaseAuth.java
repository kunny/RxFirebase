package com.androidhuman.rxfirebase.auth;

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
        return Observable.create(new AuthStateChangesOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> createUserWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return Single.create(
                new CreateUserWithEmailAndPasswordOnSubscribe(instance, email, password));
    }

    @CheckResult
    @NonNull
    public static Single<List<String>> fetchProvidersForEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return Single.create(new FetchProvidersForEmailOnSubscribe(instance, email));
    }

    @CheckResult
    @NonNull
    public static Maybe<FirebaseUser> getCurrentUser(
            @NonNull final FirebaseAuth instance) {
        return Maybe.create(new GetCurrentUserOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Completable sendPasswordResetEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return Completable.create(new SendPasswordResetEmailOnSubscribe(instance, email));
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInAnonymous(
            @NonNull FirebaseAuth instance) {
        return Single.create(new SignInAnonymousOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithCredential(
            @NonNull FirebaseAuth instance, @NonNull AuthCredential credential) {
        return Single.create(new SignInWithCredentialOnSubscribe(instance, credential));
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithCustomToken(
            @NonNull FirebaseAuth instance, @NonNull String token) {
        return Single.create(new SignInWithCustomTokenOnSubscribe(instance, token));
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return Single.create(
                new SignInWithEmailAndPasswordOnSubscribe(instance, email, password));
    }

    @CheckResult
    @NonNull
    public static Completable signOut(@NonNull FirebaseAuth instance) {
        return Completable.create(new SignOutOnSubscribe(instance));
    }

    private RxFirebaseAuth() {
        throw new AssertionError("No instances");
    }
}
