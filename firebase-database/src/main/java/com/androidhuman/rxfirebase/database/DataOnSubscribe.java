package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class DataOnSubscribe implements SingleOnSubscribe<DataSnapshot> {

    private final DatabaseReference ref;

    DataOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
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

        ref.addListenerForSingleValueEvent(listener);
    }
}
