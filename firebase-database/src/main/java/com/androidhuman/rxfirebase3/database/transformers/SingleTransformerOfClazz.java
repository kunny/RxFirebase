package com.androidhuman.rxfirebase3.database.transformers;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.functions.Function;

public final class SingleTransformerOfClazz<T>
        implements SingleTransformer<DataSnapshot, T> {

    private final Class<T> clazz;

    public SingleTransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public SingleSource<T> apply(@NonNull final Single<DataSnapshot> upstream) {
        return upstream.flatMap(new Function<DataSnapshot, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return Single.just(dataSnapshot.getValue(clazz));
                } else {
                    return Single.error(new NoSuchElementException());
                }
            }
        });
    }
}
