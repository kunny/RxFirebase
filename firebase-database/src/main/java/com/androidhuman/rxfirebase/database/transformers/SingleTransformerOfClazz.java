package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import java.util.NoSuchElementException;

import rx.Single;
import rx.functions.Func1;

public final class SingleTransformerOfClazz<T>
        implements Single.Transformer<DataSnapshot, T> {

    private final Class<T> clazz;

    public SingleTransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Single<T> call(Single<DataSnapshot> source) {
        return source.flatMap(new Func1<DataSnapshot, Single<? extends T>>() {
            @Override
            public Single<? extends T> call(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return Single.just(dataSnapshot.getValue(clazz));
                } else {
                    return Single.error(new NoSuchElementException());
                }
            }
        });
    }
}
