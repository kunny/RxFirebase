package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.NoSuchElementException;

import rx.Single;
import rx.functions.Func1;

public final class SingleTransformerOfGenericTypeIndicator<T>
        implements Single.Transformer<DataSnapshot, T> {

    private final GenericTypeIndicator<T> typeIndicator;

    public SingleTransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public Single<T> call(Single<DataSnapshot> source) {
        return source.flatMap(new Func1<DataSnapshot, Single<? extends T>>() {
            @Override
            public Single<? extends T> call(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return Single.just(dataSnapshot.getValue(typeIndicator));
                } else {
                    return Single.error(new NoSuchElementException());
                }
            }
        });
    }
}
