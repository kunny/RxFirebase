package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.Single;
import rx.SingleSubscriber;

final class FetchProvidersForEmailOnSubscribe
        implements Single.OnSubscribe<List<String>> {

    private final FirebaseAuth instance;

    private final String email;

    FetchProvidersForEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void call(final SingleSubscriber<? super List<String>> subscriber) {

        final OnCompleteListener<ProviderQueryResult> listener =
                new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (!task.isSuccessful()) {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onError(task.getException());
                            }
                            return;
                        }

                        if (!subscriber.isUnsubscribed()) {
                            List<String> providers = task.getResult().getProviders();
                            if (null == providers) {
                                // If result is null, create an empty list
                                providers = new ArrayList<>();
                            }
                            subscriber.onSuccess(providers);
                        }
                    }
                };

        instance.fetchProvidersForEmail(email)
                .addOnCompleteListener(listener);
    }
}
