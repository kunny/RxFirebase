package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

final class DataChangesOnSubscribe implements Observable.OnSubscribe<DataSnapshot> {

    private final DatabaseReference ref;

    DataChangesOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
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

        ref.addValueEventListener(listener);

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                ref.removeEventListener(listener);
            }
        }));
    }
}
