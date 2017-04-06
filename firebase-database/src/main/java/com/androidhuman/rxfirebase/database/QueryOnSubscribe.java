package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rx.Single;
import rx.SingleSubscriber;

final class QueryOnSubscribe implements Single.OnSubscribe<DataSnapshot> {

    private final Query query;

    QueryOnSubscribe(Query query) {
        this.query = query;
    }

    @Override
    public void call(final SingleSubscriber<? super DataSnapshot> subscriber) {
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(databaseError.toException());
                }
            }
        };

        query.addListenerForSingleValueEvent(listener);
    }
}
