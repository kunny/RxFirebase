package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

final class ChildEventsOnSubscribe implements ObservableOnSubscribe<ChildEvent> {

    private DatabaseReference ref;

    ChildEventsOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
    }

    @Override
    public void subscribe(final ObservableEmitter<ChildEvent> emitter) {
        final ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(ChildAddEvent.create(dataSnapshot, previousChildName));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(ChildChangeEvent.create(dataSnapshot, previousChildName));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(ChildRemoveEvent.create(dataSnapshot));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(ChildMoveEvent.create(dataSnapshot, previousChildName));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (!emitter.isDisposed()) {
                    emitter.onError(databaseError.toException());
                }
            }
        };

        ref.addChildEventListener(listener);
        emitter.setDisposable(Disposables.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ref.removeEventListener(listener);
            }
        }));
    }
}
