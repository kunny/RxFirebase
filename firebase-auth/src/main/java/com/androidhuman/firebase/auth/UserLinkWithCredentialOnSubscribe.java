package com.androidhuman.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class UserLinkWithCredentialOnSubscribe implements Observable.OnSubscribe<AuthResult> {

    private final FirebaseUser user;

    private final AuthCredential credential;

    UserLinkWithCredentialOnSubscribe(FirebaseUser user, AuthCredential credential) {
        this.user = user;
        this.credential = credential;
    }

    @Override
    public void call(final Subscriber<? super AuthResult> subscriber) {
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    subscriber.onError(task.getException());
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(task.getResult());
                    subscriber.onCompleted();
                }
            }
        };

        user.linkWithCredential(credential)
                .addOnCompleteListener(listener);
    }
}
