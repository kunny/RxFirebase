package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

final class ChildEventsOnSubscribe implements Observable.OnSubscribe<ChildEvent> {

    private DatabaseReference ref;

    ChildEventsOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
    }

    @Override
    public void call(final Subscriber<? super ChildEvent> subscriber) {
        final ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ChildAddEvent.create(dataSnapshot, previousChildName));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ChildChangeEvent.create(dataSnapshot, previousChildName));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ChildRemoveEvent.create(dataSnapshot));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ChildMoveEvent.create(dataSnapshot, previousChildName));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(databaseError.toException());
                }
            }
        };

        ref.addChildEventListener(listener);

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                ref.removeEventListener(listener);
            }
        }));
    }
}
