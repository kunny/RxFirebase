package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import com.memoizrlabs.retrooptional.Optional;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

public final class SingleTransformerOfGenericTypeIndicator<T>
        implements SingleTransformer<DataSnapshot, Optional<T>> {

    private GenericTypeIndicator<T> typeIndicator;

    public SingleTransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public SingleSource<Optional<T>> apply(Single<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, Optional<T>>() {
            @Override
            public Optional<T> apply(DataSnapshot dataSnapshot) throws Exception {
                return Optional.of(dataSnapshot.getValue(typeIndicator));
            }
        });
    }
}
