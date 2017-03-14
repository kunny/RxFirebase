package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

final class QueryChangesOnSubscribe implements Observable.OnSubscribe<DataSnapshot> {

    private final Query query;

    QueryChangesOnSubscribe(Query query) {
        this.query = query;
    }

    @Override
    public void call(final Subscriber<? super DataSnapshot> subscriber) {
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(databaseError.toException());
                }
            }
        };

        query.addValueEventListener(listener);

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                query.removeEventListener(listener);
            }
        }));
    }
}
