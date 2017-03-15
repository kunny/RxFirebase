package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import com.androidhuman.rxfirebase.database.model.DataValue;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public final class TransformerOfClazz<T>
        implements ObservableTransformer<DataSnapshot, DataValue<T>> {

    private final Class<T> clazz;

    public TransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ObservableSource<DataValue<T>> apply(Observable<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, DataValue<T>>() {
            @Override
            public DataValue<T> apply(DataSnapshot dataSnapshot) throws Exception {
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
