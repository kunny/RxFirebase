package com.androidhuman.rxfirebase2.auth;

import com.google.auto.value.AutoValue;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

@AutoValue
public abstract class PhoneAuthCodeAutoRetrievalTimeOutEvent extends PhoneAuthEvent {

    @CheckResult
    @NonNull
    static PhoneAuthCodeAutoRetrievalTimeOutEvent create(@NonNull String verificationId) {
        return new AutoValue_PhoneAuthCodeAutoRetrievalTimeOutEvent(verificationId);
    }

    @NonNull
    public abstract String verificationId();
}
