package com.androidhuman.rxfirebase.database.transformers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import com.memoizrlabs.retrooptional.Optional;

import rx.Observable;
import rx.functions.Func1;

public final class TransformerOfGenericTypeIndicator<T>
        implements Observable.Transformer<DataSnapshot, Optional<T>> {

    private GenericTypeIndicator<T> typeIndicator;

    public TransformerOfGenericTypeIndicator(GenericTypeIndicator<T> indicator) {
        this.typeIndicator = typeIndicator;
    }

    @Override
    public Observable<Optional<T>> call(Observable<DataSnapshot> source) {
        return source.map(new Func1<DataSnapshot, Optional<T>>() {
            @Override
            public Optional<T> call(DataSnapshot dataSnapshot) {
                return Optional.of(dataSnapshot.getValue(typeIndicator));
            }
        });
    }
}
