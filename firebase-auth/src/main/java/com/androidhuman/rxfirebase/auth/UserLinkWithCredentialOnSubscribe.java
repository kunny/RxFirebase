package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Single;
import rx.SingleSubscriber;

final class UserLinkWithCredentialOnSubscribe
        implements Single.OnSubscribe<FirebaseUser> {

    private final FirebaseUser user;

    private final AuthCredential credential;

    UserLinkWithCredentialOnSubscribe(FirebaseUser user, AuthCredential credential) {
        this.user = user;
        this.credential = credential;
    }

    @Override
    public void call(final SingleSubscriber<? super FirebaseUser> subscriber) {
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    subscriber.onError(task.getException());
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    FirebaseUser user = task.getResult().getUser();
                    if (null != user) {
                        subscriber.onSuccess(user);
                    } else {
                        subscriber.onError(
                                new IllegalStateException("FirebaseUser does not exists."));
                    }
                }
            }
        };

        user.linkWithCredential(credential)
                .addOnCompleteListener(listener);
    }
}
