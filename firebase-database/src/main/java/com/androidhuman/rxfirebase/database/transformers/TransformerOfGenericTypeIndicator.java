package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import rx.Observable;
import rx.functions.Func1;

public final class TransformerOfGenericTypeIndicator<T>
        implements Observable.Transformer<DataSnapshot, T> {

    private GenericTypeIndicator<T> typeIndicator;

    public TransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public Observable<T> call(Observable<DataSnapshot> source) {
        return source.flatMap(new Func1<DataSnapshot, Observable<T>>() {
            @Override
            public Observable<T> call(DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(typeIndicator);
                if (null != value) {
                    return Observable.just(value);
                } else {
                    return Observable.empty();
                }
            }
        });
    }
}
