package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.PhoneAuthProvider;

import android.app.Activity;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public final class RxPhoneAuthProvider {

    @CheckResult
    @NonNull
    public static Observable<PhoneAuthEvent> verifyPhoneNumber(
            @NonNull PhoneAuthProvider provider, String phoneNumber,
            long timeout, TimeUnit timeUnit, Activity activity) {
        return verifyPhoneNumber(provider, phoneNumber,
                timeout, timeUnit, activity, null);
    }

    @CheckResult
    @NonNull
    public static Observable<PhoneAuthEvent> verifyPhoneNumber(
            @NonNull PhoneAuthProvider provider, String phoneNumber,
            long timeout, TimeUnit timeUnit, Activity activity,
            @Nullable PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        return new PhoneAuthProviderVerifyPhoneNumberActivityObserver(
                provider, phoneNumber, timeout, timeUnit, activity, forceResendingToken);
    }

    @CheckResult
    @NonNull
    public static Observable<PhoneAuthEvent> verifyPhoneNumber(
            @NonNull PhoneAuthProvider provider, String phoneNumber,
            long timeout, TimeUnit timeUnit, Executor executor) {
        return verifyPhoneNumber(provider, phoneNumber,
                timeout, timeUnit, executor, null);
    }

    @CheckResult
    @NonNull
    public static Observable<PhoneAuthEvent> verifyPhoneNumber(
            @NonNull PhoneAuthProvider provider, String phoneNumber,
            long timeout, TimeUnit timeUnit, Executor executor,
            @Nullable PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        return new PhoneAuthProviderVerifyPhoneNumberExecutorObserver(
                provider, phoneNumber, timeout, timeUnit, executor, forceResendingToken);
    }
}
