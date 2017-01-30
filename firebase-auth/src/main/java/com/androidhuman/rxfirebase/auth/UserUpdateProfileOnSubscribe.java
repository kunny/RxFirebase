package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import android.support.annotation.NonNull;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class UserUpdateProfileOnSubscribe implements CompletableOnSubscribe {

    private final FirebaseUser user;

    private final UserProfileChangeRequest request;

    UserUpdateProfileOnSubscribe(FirebaseUser user, UserProfileChangeRequest request) {
        this.user = user;
        this.request = request;
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

        user.updateProfile(request)
                .addOnCompleteListener(listener);
    }
}
