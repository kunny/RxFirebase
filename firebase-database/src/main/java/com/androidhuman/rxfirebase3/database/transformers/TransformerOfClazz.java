package com.androidhuman.rxfirebase3.database.transformers;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.database.model.DataValue;
import com.google.firebase.database.DataSnapshot;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Function;

public final class TransformerOfClazz<T>
        implements ObservableTransformer<DataSnapshot, DataValue<T>> {

    private final Class<T> clazz;

    public TransformerOfClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ObservableSource<DataValue<T>> apply(@NonNull final Observable<DataSnapshot> upstream) {
        return upstream.map(new Function<DataSnapshot, DataValue<T>>() {
            @Override
            public DataValue<T> apply(@NonNull final DataSnapshot dataSnapshot) {
                DataValue<T> result;
                if (dataSnapshot.exists()) {
                    result = DataValue.of(dataSnapshot.getValue(clazz));
                } else {
                    result = DataValue.empty();
                }
                return result;
            }
        });
    }
}
