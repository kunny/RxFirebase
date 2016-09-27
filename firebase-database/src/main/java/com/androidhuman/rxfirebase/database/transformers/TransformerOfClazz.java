package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import com.memoizrlabs.retrooptional.Optional;

import rx.Observable;
import rx.functions.Func1;

public final class TransformerOfClazz<T>
        implements Observable.Transformer<DataSnapshot, Optional<T>> {

    private final Class<T> clazz;

    public TransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Observable<Optional<T>> call(Observable<DataSnapshot> source) {
        return source.map(new Func1<DataSnapshot, Optional<T>>() {
            @Override
            public Optional<T> call(DataSnapshot dataSnapshot) {
                return Optional.of(dataSnapshot.getValue(clazz));
            }
        });
    }
}
