package com.androidhuman.rxfirebase2.storage;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import android.net.Uri;
import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class DownloadUrlObserver extends Single<Uri> {

    private final StorageReference reference;

    DownloadUrlObserver(@NonNull StorageReference ref) {
        this.reference = ref;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super Uri> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        reference.getDownloadUrl()
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<Uri> {

        private final SingleObserver<? super Uri> observer;

        Listener(@NonNull SingleObserver<? super Uri> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onSuccess(task.getResult());
                }
            }
        }
    }
}
