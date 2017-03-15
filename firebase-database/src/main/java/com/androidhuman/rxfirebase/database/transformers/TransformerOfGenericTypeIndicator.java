package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import com.androidhuman.rxfirebase.database.model.DataValue;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public final class TransformerOfGenericTypeIndicator<T>
        implements ObservableTransformer<DataSnapshot, DataValue<T>> {

    private GenericTypeIndicator<T> typeIndicator;

    public TransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public ObservableSource<DataValue<T>> apply(Observable<DataSnapshot> upstream) {
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
