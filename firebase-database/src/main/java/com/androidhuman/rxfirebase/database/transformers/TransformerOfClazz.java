package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import com.memoizrlabs.retrooptional.Function1;
import com.memoizrlabs.retrooptional.Optional;

import rx.Observable;
import rx.functions.Func1;

public final class TransformerOfClazz<T>
        implements Observable.Transformer<Optional<DataSnapshot>, Optional<T>> {

    private final Class<T> clazz;

    public TransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Observable<Optional<T>> call(Observable<Optional<DataSnapshot>> source) {
        return source.map(new Func1<Optional<DataSnapshot>, Optional<T>>() {
            @Override
            public Optional<T> call(Optional<DataSnapshot> dataSnapshot) {
                return dataSnapshot.flatMap(new Function1<DataSnapshot, Optional<T>>() {
                    @Override
                    public Optional<T> apply(DataSnapshot dataSnapshot) {
                        return Optional.of(dataSnapshot.getValue(clazz));
                    }
                });
            }
        });
    }
}
