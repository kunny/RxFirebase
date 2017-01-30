package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class UserUpdatePasswordOnSubscribe implements CompletableOnSubscribe {

    private final FirebaseUser user;

    private final String password;

    UserUpdatePasswordOnSubscribe(FirebaseUser user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) {
        OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!emitter.isDisposed()) {
                    if (!task.isSuccessful()) {
                        emitter.onError(task.getException());
                    } else {
                        emitter.onComplete();
                    }
                }
            }
        };

        user.updatePassword(password)
                .addOnCompleteListener(listener);
    }
}
