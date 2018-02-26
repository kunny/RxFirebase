package com.androidhuman.rxfirebase2.auth;

import com.google.auto.value.AutoValue;
import com.google.firebase.auth.PhoneAuthProvider;

import android.support.annotation.NonNull;

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
