package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class UserGetTokenOnSubscribe implements SingleOnSubscribe<String> {

    private final FirebaseUser user;

    private final boolean forceRefresh;

    UserGetTokenOnSubscribe(FirebaseUser user, boolean forceRefresh) {
        this.user = user;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void subscribe(final SingleEmitter<String> emitter) {
        OnCompleteListener<GetTokenResult> listener = new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (!task.isSuccessful()) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(task.getException());
                    }
                    return;
                }

                if (!emitter.isDisposed()) {
                    emitter.onSuccess(task.getResult().getToken());
                }
            }
        };

        user.getToken(forceRefresh)
                .addOnCompleteListener(listener);
    }
}
