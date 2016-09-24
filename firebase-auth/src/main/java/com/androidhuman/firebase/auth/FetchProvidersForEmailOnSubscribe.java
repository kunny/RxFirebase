package com.androidhuman.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import com.memoizrlabs.retrooptional.Function1;
import com.memoizrlabs.retrooptional.Optional;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

final class FetchProvidersForEmailOnSubscribe
        implements Observable.OnSubscribe<Optional<List<String>>> {

    private final FirebaseAuth instance;

    private final String email;

    FetchProvidersForEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void call(final Subscriber<? super Optional<List<String>>> subscriber) {

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
                            subscriber.onNext(Optional.of(task.getResult())
                                    .map(new Function1<ProviderQueryResult, List<String>>() {
                                        @Override
                                        public List<String> apply(ProviderQueryResult r) {
                                            return r.getProviders();
                                        }
                                    }));
                            subscriber.onCompleted();
                        }
                    }
                };

        instance.fetchProvidersForEmail(email)
                .addOnCompleteListener(listener);
    }
}
