package com.androidhuman.rxfirebase2.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.NoSuchElementException;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class SingleTransformerOfGenericTypeIndicator<T>
        implements SingleTransformer<DataSnapshot, T> {

    private GenericTypeIndicator<T> typeIndicator;

    public SingleTransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public SingleSource<T> apply(Single<DataSnapshot> upstream) {
        return upstream.flatMap(new Function<DataSnapshot, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return Single.just(dataSnapshot.getValue(typeIndicator));
                } else {
                    return Single.error(new NoSuchElementException());
                }
            }
        });
    }
}
