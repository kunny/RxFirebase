package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;

public final class PhoneAuthProviderVerifyPhoneNumberActivityObserver
        extends Observable<PhoneAuthEvent> {

    private final PhoneAuthProvider provider;

    private final String phoneNumber;

    private final long timeOut;

    private final TimeUnit timeUnit;

    private final Activity activity;

    @Nullable
    private final PhoneAuthProvider.ForceResendingToken forceResendingToken;

    public PhoneAuthProviderVerifyPhoneNumberActivityObserver(
            PhoneAuthProvider provider, String phoneNumber,
            long timeOut, TimeUnit timeUnit, Activity activity,
            @Nullable PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
        this.activity = activity;
        this.forceResendingToken = forceResendingToken;
    }

    @Override
    protected void subscribeActual(Observer<? super PhoneAuthEvent> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        if (null == forceResendingToken) {
            provider.verifyPhoneNumber(phoneNumber, timeOut, timeUnit,
                    activity, listener);
        } else {
            provider.verifyPhoneNumber(phoneNumber, timeOut, timeUnit,
                    activity, listener, forceResendingToken);
        }
    }

    static final class Listener extends OnVerificationStateChangedDisposable {

        private final Observer<? super PhoneAuthEvent> observer;

        Listener(@NonNull Observer<? super PhoneAuthEvent> observer) {
            this.observer = observer;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String verificationId) {
            if (!isDisposed()) {
                observer.onNext(PhoneAuthCodeAutoRetrievalTimeOutEvent.create(verificationId));
                observer.onComplete();
            }
        }

        @Override
        public void onCodeSent(String verificationId,
                PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            if (!isDisposed()) {
                observer.onNext(PhoneAuthCodeSentEvent.create(verificationId, forceResendingToken));
            }
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            if (!isDisposed()) {
                observer.onNext(PhoneAuthVerificationCompleteEvent.create(credential));
                observer.onComplete();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (!isDisposed()) {
                observer.onError(e);
            }
        }
    }
}
