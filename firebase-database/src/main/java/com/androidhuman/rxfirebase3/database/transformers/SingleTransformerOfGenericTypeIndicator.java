package com.androidhuman.rxfirebase3.database.transformers;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.functions.Function;

public final class SingleTransformerOfGenericTypeIndicator<T>
        implements SingleTransformer<DataSnapshot, T> {

    private final GenericTypeIndicator<T> typeIndicator;

    public SingleTransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public SingleSource<T> apply(@NonNull final Single<DataSnapshot> upstream) {
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
