package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import com.memoizrlabs.retrooptional.Optional;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public final class ObsTransformerOfClazz<T>
        implements ObservableTransformer<DataSnapshot, Optional<T>> {

    private final Class<T> clazz;

    public ObsTransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ObservableSource<Optional<T>> apply(Observable<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, Optional<T>>() {
            @Override
            public Optional<T> apply(DataSnapshot dataSnapshot) throws Exception {
                return Optional.of(dataSnapshot.getValue(clazz));
            }
        });
    }
}
