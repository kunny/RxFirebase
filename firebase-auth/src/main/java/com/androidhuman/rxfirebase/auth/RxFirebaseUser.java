package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Completable;
import rx.Single;

public final class RxFirebaseUser {

    @CheckResult
    @NonNull
    public static Completable delete(@NonNull FirebaseUser user) {
        return Completable.create(new UserDeleteOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Single<String> getToken(
            @NonNull FirebaseUser user, boolean forceRefresh) {
        return Single.create(new UserGetTokenOnSubscribe(user, forceRefresh));
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> linkWithCredential(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return Single.create(new UserLinkWithCredentialOnSubscribe(user, credential));
    }

    @CheckResult
    @NonNull
    public static Completable reauthenticate(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return Completable.create(new UserReauthenticateOnSubscribe(user, credential));
    }

    @CheckResult
    @NonNull
    public static Completable reload(@NonNull FirebaseUser user) {
        return Completable.create(new UserReloadOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Completable sendEmailVerification(@NonNull FirebaseUser user) {
        return Completable.create(new UserSendEmailVerificationOnSubscribe(user));
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> unlink(
            @NonNull FirebaseUser user, @NonNull String provider) {
        return Single.create(new UserUnlinkOnSubscribe(user, provider));
    }

    @CheckResult
    @NonNull
    public static Completable updateEmail(
            @NonNull FirebaseUser user, @NonNull String email) {
        return Completable.create(new UserUpdateEmailOnSubscribe(user, email));
    }

    @CheckResult
    @NonNull
    public static Completable updatePassword(
            @NonNull FirebaseUser user, @NonNull String password) {
        return Completable.create(new UserUpdatePasswordOnSubscribe(user, password));
    }

    @CheckResult
    @NonNull
    public static Completable updateProfile(
            @NonNull FirebaseUser user, @NonNull UserProfileChangeRequest request) {
        return Completable.create(new UserUpdateProfileOnSubscribe(user, request));
    }

    private RxFirebaseUser() {
        throw new AssertionError("No instances");
    }
}
