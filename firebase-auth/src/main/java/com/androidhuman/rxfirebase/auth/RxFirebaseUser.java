package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Single;


public final class RxFirebaseUser {

    @CheckResult
    @NonNull
    public static Single<TaskResult> delete(@NonNull FirebaseUser user) {
        return Single.create(new UserDeleteOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Single<String> getToken(
            @NonNull FirebaseUser user, boolean forceRefresh) {
        return Single.create(new UserGetTokenOnSubscribe(user, forceRefresh));
    }

    @CheckResult
    @NonNull
    public static Single<AuthResult> linkWithCredential(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return Single.create(new UserLinkWithCredentialOnSubscribe(user, credential));
    }

    @CheckResult
    @NonNull
    public static Single<TaskResult> reauthenticate(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return Single.create(new UserReauthenticateOnSubscribe(user, credential));
    }

    @CheckResult
    @NonNull
    public static Single<TaskResult> reload(@NonNull FirebaseUser user) {
        return Single.create(new UserReloadOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Single<TaskResult> sendEmailVerification(@NonNull FirebaseUser user) {
        return Single.create(new UserSendEmailVerificationOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Single<AuthResult> unlink(
            @NonNull FirebaseUser user, @NonNull String provider) {
        return Single.create(new UserUnlinkOnSubscribe(user, provider));
    }

    @CheckResult
    @NonNull
    public static Single<TaskResult> updateEmail(
            @NonNull FirebaseUser user, @NonNull String email) {
        return Single.create(new UserUpdateEmailOnSubscribe(user, email));
    }

    @CheckResult
    @NonNull
    public static Single<TaskResult> updatePassword(
            @NonNull FirebaseUser user, @NonNull String password) {
        return Single.create(new UserUpdatePasswordOnSubscribe(user, password));
    }

    @CheckResult
    @NonNull
    public static Single<TaskResult> updateProfile(
            @NonNull FirebaseUser user, @NonNull UserProfileChangeRequest request) {
        return Single.create(new UserUpdateProfileOnSubscribe(user, request));
    }

    private RxFirebaseUser() {
        throw new AssertionError("No instances");
    }
}
