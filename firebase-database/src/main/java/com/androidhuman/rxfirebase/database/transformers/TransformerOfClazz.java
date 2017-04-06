package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import com.androidhuman.rxfirebase.database.model.DataValue;

import rx.Observable;
import rx.functions.Func1;

public final class TransformerOfClazz<T>
        implements Observable.Transformer<DataSnapshot, DataValue<T>> {

    private final Class<T> clazz;

    public TransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Observable<DataValue<T>> call(Observable<DataSnapshot> source) {
        return source.map(new Func1<DataSnapshot, DataValue<T>>() {
            @Override
            public DataValue<T> call(DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(clazz);
                DataValue<T> result;
                if (null != value) {
                    result = DataValue.of(value);
                } else {
                    result = DataValue.empty();
                }
                return result;
            }
        });
    }
}
