package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import rx.Single;
import rx.SingleSubscriber;

final class DataOnSubscribe implements Single.OnSubscribe<DataSnapshot> {

    private final DatabaseReference ref;

    DataOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
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

        ref.addListenerForSingleValueEvent(listener);
    }
}
