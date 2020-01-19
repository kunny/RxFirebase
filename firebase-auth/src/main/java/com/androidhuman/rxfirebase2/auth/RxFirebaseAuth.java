package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;


public final class RxFirebaseAuth {

    @CheckResult
    @NonNull
    public static Observable<FirebaseAuth> authStateChanges(@NonNull FirebaseAuth instance) {
        return new AuthStateChangesObserver(instance);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> createUserWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return new CreateUserWithEmailAndPasswordObserver(instance, email, password);
    }

    @CheckResult
    @NonNull
    public static Maybe<List<String>> fetchSignInMethodsForEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return new FetchSignInMethodsForEmailObserver(instance, email);
    }

    @CheckResult
    @NonNull
    public static Maybe<FirebaseUser> getCurrentUser(
            @NonNull final FirebaseAuth instance) {
        return new GetCurrentUserObserver(instance);
    }

    @CheckResult
    @NonNull
    public static Completable sendPasswordResetEmail(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        return new SendPasswordResetEmailObserver(instance, email);
    }

    @CheckResult
    @NonNull
    public static Single<AuthResult> signInAnonymouslyAuthResult(
            @NonNull FirebaseAuth instance) {
        return new SignInAnonymouslyObserver(instance);
    }

    @CheckResult
    @NonNull
    public static Single<AuthResult> signInWithCredentialAuthResult(
            @NonNull FirebaseAuth instance, @NonNull AuthCredential credential) {
        return new SignInWithCredentialObserver(instance, credential);
    }

    @CheckResult
    @NonNull
    public static Single<AuthResult> signInWithCustomTokenAuthResult(
            @NonNull FirebaseAuth instance, @NonNull String token) {
        return new SignInWithCustomTokenObserver(instance, token);
    }

    @CheckResult
    @NonNull
    public static Single<AuthResult> signInWithEmailAndPasswordAuthResult(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return new SignInWithEmailAndPasswordObserver(instance, email, password);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInAnonymously(
            @NonNull FirebaseAuth instance) {
        return new SignInAnonymouslyObserver(instance)
                .map(new Function<AuthResult, FirebaseUser>() {
                    @SuppressWarnings("RedundantThrows")
                    @Override
                    public FirebaseUser apply(AuthResult authResult) throws Exception {
                        return authResult.getUser();
                    }
                });
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithCredential(
            @NonNull FirebaseAuth instance, @NonNull AuthCredential credential) {
        return new SignInWithCredentialObserver(instance, credential)
                .map(new Function<AuthResult, FirebaseUser>() {
                    @SuppressWarnings("RedundantThrows")
                    @Override
                    public FirebaseUser apply(AuthResult authResult) throws Exception {
                        return authResult.getUser();
                    }
                });
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithCustomToken(
            @NonNull FirebaseAuth instance, @NonNull String token) {
        return new SignInWithCustomTokenObserver(instance, token)
                .map(new Function<AuthResult, FirebaseUser>() {
                    @SuppressWarnings("RedundantThrows")
                    @Override
                    public FirebaseUser apply(AuthResult authResult) throws Exception {
                        return authResult.getUser();
                    }
                });
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> signInWithEmailAndPassword(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String password) {
        return new SignInWithEmailAndPasswordObserver(instance, email, password)
                .map(new Function<AuthResult, FirebaseUser>() {
                    @SuppressWarnings("RedundantThrows")
                    @Override
                    public FirebaseUser apply(AuthResult authResult) throws Exception {
                        return authResult.getUser();
                    }
                });
    }

    @CheckResult
    @NonNull
    public static Single<AuthResult> signInWithEmailLink(
            @NonNull FirebaseAuth instance, @NonNull String email, @NonNull String emailLink) {
        return new SignInWithEmailLinkObserver(instance, email, emailLink);
    }

    @CheckResult
    @NonNull
    public static Completable signOut(@NonNull FirebaseAuth instance) {
        return new SignOutObserver(instance);
    }

    @CheckResult
    @NonNull
    public static Completable updateCurrentUser(
            @NonNull FirebaseAuth instance, @NonNull FirebaseUser user) {
        return new UpdateCurrentUserObserver(instance, user);
    }

    private RxFirebaseAuth() {
        throw new AssertionError("No instances");
    }
}
