package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import rx.Observable;
import rx.functions.Func1;

public final class TransformerOfClazz<T>
        implements Observable.Transformer<DataSnapshot, T> {

    private final Class<T> clazz;

    public TransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Observable<T> call(Observable<DataSnapshot> source) {
        return source.flatMap(new Func1<DataSnapshot, Observable<T>>() {
            @Override
            public Observable<T> call(DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(clazz);
                if (null != value) {
                    return Observable.just(value);
                } else {
                    return Observable.empty();
                }
            }
        });
    }
}
