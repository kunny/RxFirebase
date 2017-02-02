package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

final class DataChangesOnSubscribe implements ObservableOnSubscribe<DataSnapshot> {

    private final DatabaseReference ref;

    DataChangesOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
    }

    @Override
    public void subscribe(final ObservableEmitter<DataSnapshot> emitter) {
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (!emitter.isDisposed()) {
                    emitter.onError(databaseError.toException());
                }
            }
        };

        ref.addValueEventListener(listener);
        emitter.setDisposable(Disposables.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ref.removeEventListener(listener);
            }
        }));
    }
}
