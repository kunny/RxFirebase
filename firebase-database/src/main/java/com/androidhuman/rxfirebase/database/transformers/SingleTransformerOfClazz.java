package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;

import com.androidhuman.rxfirebase.database.model.DataValue;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

public final class SingleTransformerOfClazz<T>
        implements SingleTransformer<DataSnapshot, DataValue<T>> {

    private final Class<T> clazz;

    public SingleTransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public SingleSource<DataValue<T>> apply(Single<DataSnapshot> upstream) {
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
