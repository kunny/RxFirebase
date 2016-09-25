package com.androidhuman.firebase.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.androidhuman.firebase.auth.model.TaskResult;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Observable;

public final class RxFirebaseUser {

    @CheckResult
    @NonNull
    public static Observable<TaskResult> delete(@NonNull FirebaseUser user) {
        return Observable.create(new UserDeleteOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Observable<String> getToken(
            @NonNull FirebaseUser user, boolean forceRefresh) {
        return Observable.create(new UserGetTokenOnSubscribe(user, forceRefresh));
    }

    @CheckResult
    @NonNull
    public static Observable<AuthResult> linkWithCredential(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return Observable.create(new UserLinkWithCredentialOnSubscribe(user, credential));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> reauthenticate(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return Observable.create(new UserReauthenticateOnSubscribe(user, credential));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> reload(@NonNull FirebaseUser user) {
        return Observable.create(new UserReloadOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> sendEmailVerification(@NonNull FirebaseUser user) {
        return Observable.create(new UserSendEmailVerificationOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Observable<AuthResult> unlink(
            @NonNull FirebaseUser user, @NonNull String provider) {
        return Observable.create(new UserUnlinkOnSubscribe(user, provider));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> updateEmail(
            @NonNull FirebaseUser user, @NonNull String email) {
        return Observable.create(new UserUpdateEmailOnSubscribe(user, email));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> updatePassword(
            @NonNull FirebaseUser user, @NonNull String password) {
        return Observable.create(new UserUpdatePasswordOnSubscribe(user, password));
    }

    @CheckResult
    @NonNull
    public static Observable<TaskResult> updateProfile(
            @NonNull FirebaseUser user, @NonNull UserProfileChangeRequest request) {
        return Observable.create(new UserUpdateProfileOnSubscribe(user, request));
    }

    private RxFirebaseUser() {
        throw new AssertionError("No instances");
    }
}
