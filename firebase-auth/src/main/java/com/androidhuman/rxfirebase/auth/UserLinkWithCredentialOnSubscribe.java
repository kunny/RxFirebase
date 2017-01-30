package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class UserLinkWithCredentialOnSubscribe implements SingleOnSubscribe<AuthResult> {

    private final FirebaseUser user;

    private final AuthCredential credential;

    UserLinkWithCredentialOnSubscribe(FirebaseUser user, AuthCredential credential) {
        this.user = user;
        this.credential = credential;
    }

    @Override
    public void subscribe(final SingleEmitter<AuthResult> emitter) {
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    emitter.onError(task.getException());
                    return;
                }

                if (!emitter.isDisposed()) {
                    emitter.onSuccess(task.getResult());
                }
            }
        };

        user.linkWithCredential(credential)
                .addOnCompleteListener(listener);
    }
}
