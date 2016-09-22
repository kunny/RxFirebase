package com.androidhuman.firebase.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.firebase.auth.model.OptionalFirebaseUser;
import com.androidhuman.firebase.auth.model.TaskResult;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

public final class RxFirebaseAuth {

    @CheckResult
    @NonNull
    public static Observable<FirebaseAuth> authStateChanges(@NonNull FirebaseAuth instance) {
        return Observable.create(new AuthStateChangesOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Observable<OptionalFirebaseUser> createUserWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return Observable.create(
                new CreateUserWithEmailAndPasswordOnSubscribe(instance, email, password));
    }

    @CheckResult
    @NonNull
    public static Observable<OptionalFirebaseUser> getCurrentUser(
            @NonNull final FirebaseAuth instance) {
        return Observable.create(new Observable.OnSubscribe<OptionalFirebaseUser>() {
            @Override
            public void call(Subscriber<? super OptionalFirebaseUser> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(OptionalFirebaseUser.of(instance.getCurrentUser()));
                    subscriber.onCompleted();
                }
            }
        });
    }

    @CheckResult
    @NonNull
    public static Observable<OptionalFirebaseUser> signInAnonymous(@NonNull FirebaseAuth instance) {
        return Observable.create(new SignInAnonymousOnSubscribe(instance));
    }

    @CheckResult
    @NonNull
    public static Observable<OptionalFirebaseUser> signInWithCredential(
            @NonNull FirebaseAuth instance, @NonNull AuthCredential credential) {
        return Observable.create(new SignInWithCredentialOnSubscribe(instance, credential));
    }

    @CheckResult
    @NonNull
    public static Observable<OptionalFirebaseUser> signInWithCustomToken(
            @NonNull FirebaseAuth instance, @NonNull String token) {
        return Observable.create(new SignInWithCustomTokenOnSubscribe(instance, token));
    }

    @CheckResult
    @NonNull
    public static Observable<OptionalFirebaseUser> signInWithEmailAndPassword(
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
