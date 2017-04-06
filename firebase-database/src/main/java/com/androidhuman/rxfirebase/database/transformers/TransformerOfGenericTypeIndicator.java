package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import com.androidhuman.rxfirebase.database.model.DataValue;

import rx.Observable;
import rx.functions.Func1;

public final class TransformerOfGenericTypeIndicator<T>
        implements Observable.Transformer<DataSnapshot, DataValue<T>> {

    private final GenericTypeIndicator<T> typeIndicator;

    public TransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public Observable<DataValue<T>> call(Observable<DataSnapshot> source) {
        return source.map(new Func1<DataSnapshot, DataValue<T>>() {
            @Override
            public DataValue<T> call(DataSnapshot dataSnapshot) {
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
