package com.androidhuman.rxfirebase2.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import com.androidhuman.rxfirebase2.database.model.DataValue;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

public final class SingleTransformerOfGenericTypeIndicator<T>
        implements SingleTransformer<DataSnapshot, DataValue<T>> {

    private GenericTypeIndicator<T> typeIndicator;

    public SingleTransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public SingleSource<DataValue<T>> apply(Single<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, DataValue<T>>() {
            @Override
            public DataValue<T> apply(DataSnapshot dataSnapshot) throws Exception {
                T value = dataSnapshot.getValue(typeIndicator);
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
