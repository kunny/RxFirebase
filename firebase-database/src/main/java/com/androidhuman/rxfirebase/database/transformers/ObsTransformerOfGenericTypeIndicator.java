package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import com.memoizrlabs.retrooptional.Optional;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public final class ObsTransformerOfGenericTypeIndicator<T>
        implements ObservableTransformer<DataSnapshot, Optional<T>> {

    private GenericTypeIndicator<T> typeIndicator;

    public ObsTransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public ObservableSource<Optional<T>> apply(Observable<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, Optional<T>>() {
            @Override
            public Optional<T> apply(DataSnapshot dataSnapshot) throws Exception {
                return Optional.of(dataSnapshot.getValue(typeIndicator));
            }
        });
    }
}
