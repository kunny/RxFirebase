package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class FetchProvidersForEmailOnSubscribe
        implements SingleOnSubscribe<List<String>> {

    private final FirebaseAuth instance;

    private final String email;

    FetchProvidersForEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void subscribe(final SingleEmitter<List<String>> emitter) {

        final OnCompleteListener<ProviderQueryResult> listener =
                new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (!task.isSuccessful()) {
                            if (!emitter.isDisposed()) {
                                emitter.onError(task.getException());
                            }
                            return;
                        }

                        if (!emitter.isDisposed()) {
                            List<String> providers = task.getResult().getProviders();
                            if (null == providers) {
                                // If result is null, create an empty list
                                providers = new ArrayList<>();
                            }
                            emitter.onSuccess(providers);
                        }
                    }
                };

        instance.fetchProvidersForEmail(email)
                .addOnCompleteListener(listener);
    }
}
