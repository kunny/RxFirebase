package com.androidhuman.rxfirebase3.auth;

import com.google.auto.value.AutoValue;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

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
