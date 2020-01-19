package com.androidhuman.rxfirebase2.auth;

import com.google.auto.value.AutoValue;
import com.google.firebase.auth.PhoneAuthCredential;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

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
