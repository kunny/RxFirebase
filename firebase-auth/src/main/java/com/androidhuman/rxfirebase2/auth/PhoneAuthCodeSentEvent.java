package com.androidhuman.rxfirebase2.auth;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.firebase.auth.PhoneAuthProvider;

@AutoValue
public abstract class PhoneAuthCodeSentEvent extends PhoneAuthEvent {

    static PhoneAuthCodeSentEvent create(
            @NonNull String verificationId,
            @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        return new AutoValue_PhoneAuthCodeSentEvent(verificationId, forceResendingToken);
    }

    @NonNull
    public abstract String verificationId();

    @NonNull
    public abstract PhoneAuthProvider.ForceResendingToken forceResendingToken();
}
