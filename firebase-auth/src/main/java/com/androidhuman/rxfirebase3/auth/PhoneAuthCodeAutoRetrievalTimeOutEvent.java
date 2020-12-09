package com.androidhuman.rxfirebase3.auth;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

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
