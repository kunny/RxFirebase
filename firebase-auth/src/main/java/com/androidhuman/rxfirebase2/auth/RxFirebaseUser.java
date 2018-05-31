package com.androidhuman.rxfirebase2.auth;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;

import io.reactivex.Completable;
import io.reactivex.Single;


public final class RxFirebaseUser {

    private RxFirebaseUser() {
        throw new AssertionError("No instances");
    }

    @CheckResult
    @NonNull
    public static Completable delete(@NonNull FirebaseUser user) {
        return new UserDeleteObserver(user);
    }

    @CheckResult
    @NonNull
    public static Single<String> getIdToken(
            @NonNull FirebaseUser user, boolean forceRefresh) {
        return new UserGetIdTokenObserver(user, forceRefresh);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> linkWithCredential(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return new UserLinkWithCredentialObserver(user, credential);
    }

    @CheckResult
    @NonNull
    public static Completable reauthenticate(
            @NonNull FirebaseUser user, @NonNull AuthCredential credential) {
        return new UserReauthenticateObserver(user, credential);
    }

    @CheckResult
    @NonNull
    public static Completable reload(@NonNull FirebaseUser user) {
        return new UserReloadObserver(user);
    }

    @CheckResult
    @NonNull
    public static Completable sendEmailVerification(@NonNull FirebaseUser user) {
        return new UserSendEmailVerificationObserver(user);
    }

    @CheckResult
    @NonNull
    public static Single<FirebaseUser> unlink(
            @NonNull FirebaseUser user, @NonNull String provider) {
        return new UserUnlinkObserver(user, provider);
    }

    @CheckResult
    @NonNull
    public static Completable updateEmail(
            @NonNull FirebaseUser user, @NonNull String email) {
        return new UserUpdateEmailObserver(user, email);
    }

    @CheckResult
    @NonNull
    public static Completable updatePassword(
            @NonNull FirebaseUser user, @NonNull String password) {
        return new UserUpdatePasswordObserver(user, password);
    }

    @CheckResult
    @NonNull
    public static Completable updatePhoneNumber(
            @NonNull FirebaseUser user, @NonNull PhoneAuthCredential credential) {
        return new UserUpdatePhoneNumberObserver(user, credential);
    }

    @CheckResult
    @NonNull
    public static Completable updateProfile(
            @NonNull FirebaseUser user, @NonNull UserProfileChangeRequest request) {
        return new UserUpdateProfileObserver(user, request);
    }
}
