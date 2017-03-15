package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class QueryOnSubscribe implements SingleOnSubscribe<DataSnapshot> {

    private final Query query;

    QueryOnSubscribe(Query query) {
        this.query = query;
    }

    @Override
    public void subscribe(final SingleEmitter<DataSnapshot> emitter) {
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (!emitter.isDisposed()) {
                    emitter.onError(databaseError.toException());
                }
            }
        };

        query.addListenerForSingleValueEvent(listener);
    }
}
