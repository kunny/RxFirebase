package com.androidhuman.rxfirebase3.auth;

import com.google.auto.value.AutoValue;
import com.google.firebase.auth.PhoneAuthCredential;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

@AutoValue
public abstract class PhoneAuthVerificationCompleteEvent extends PhoneAuthEvent {

    @CheckResult
    @NonNull
    public static PhoneAuthVerificationCompleteEvent create(
            @NonNull PhoneAuthCredential credential) {
        return new AutoValue_PhoneAuthVerificationCompleteEvent(credential);
    }

    @NonNull
    public abstract PhoneAuthCredential credential();
}
