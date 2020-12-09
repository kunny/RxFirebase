package com.androidhuman.rxfirebase3.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import com.androidhuman.rxfirebase3.database.model.DataValue;

import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Function;

public final class TransformerOfGenericTypeIndicator<T>
        implements ObservableTransformer<DataSnapshot, DataValue<T>> {

    private final GenericTypeIndicator<T> typeIndicator;

    public TransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = indicator;
    }

    @Override
    public ObservableSource<DataValue<T>> apply(@NonNull final Observable<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, DataValue<T>>() {
            @Override
            public DataValue<T> apply(@NonNull final DataSnapshot dataSnapshot) {
                DataValue<T> result;
                if (dataSnapshot.exists()) {
                    result = DataValue.of(dataSnapshot.getValue(typeIndicator));
                } else {
                    result = DataValue.empty();
                }
                return result;
            }
        });
    }
}
