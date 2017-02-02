package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import com.memoizrlabs.retrooptional.Function1;
import com.memoizrlabs.retrooptional.Optional;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class FetchProvidersForEmailOnSubscribe
        implements SingleOnSubscribe<Optional<List<String>>> {

    private final FirebaseAuth instance;

    private final String email;

    FetchProvidersForEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void subscribe(final SingleEmitter<Optional<List<String>>> emitter) {

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
                            emitter.onSuccess(Optional.of(task.getResult())
                                    .map(new Function1<ProviderQueryResult, List<String>>() {
                                        @Override
                                        public List<String> apply(ProviderQueryResult r) {
                                            return r.getProviders();
                                        }
                                    }));
                        }
                    }
                };

        instance.fetchProvidersForEmail(email)
                .addOnCompleteListener(listener);
    }
}
