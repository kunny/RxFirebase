package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class UserUnlinkOnSubscribe implements SingleOnSubscribe<FirebaseUser> {

    private final FirebaseUser user;

    private final String provider;

    UserUnlinkOnSubscribe(FirebaseUser user, String provider) {
        this.user = user;
        this.provider = provider;
    }

    @Override
    public void subscribe(final SingleEmitter<FirebaseUser> emitter) {
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(task.getException());
                    }
                    return;
                }

                if (!emitter.isDisposed()) {
                    FirebaseUser user = task.getResult().getUser();
                    if (null != user) {
                        emitter.onSuccess(user);
                    } else {
                        emitter.onError(new IllegalStateException("FirebaseUser does not exists."));
                    }
                }
            }
        };

        user.unlink(provider)
                .addOnCompleteListener(listener);
    }
}
