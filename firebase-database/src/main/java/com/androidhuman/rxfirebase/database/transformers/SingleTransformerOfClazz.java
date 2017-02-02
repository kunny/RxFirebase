package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import com.memoizrlabs.retrooptional.Optional;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

public final class SingleTransformerOfClazz<T>
        implements SingleTransformer<DataSnapshot, Optional<T>> {

    private final Class<T> clazz;

    public SingleTransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public SingleSource<Optional<T>> apply(Single<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, Optional<T>>() {
            @Override
            public Optional<T> apply(DataSnapshot dataSnapshot) throws Exception {
                return Optional.of(dataSnapshot.getValue(clazz));
            }
        });
    }
}
