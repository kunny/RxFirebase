package com.androidhuman.rxfirebase3.storage;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;

import android.net.Uri;
import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class PutFileObserver extends Single<UploadTask.TaskSnapshot> {

    private final StorageReference reference;

    private final Uri uri;

    PutFileObserver(@NonNull StorageReference ref, @NonNull Uri uri) {
        this.reference = ref;
        this.uri = uri;
    }

    @Override
    protected void subscribeActual(
            @NonNull final SingleObserver<? super UploadTask.TaskSnapshot> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        reference.putFile(uri)
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<UploadTask.TaskSnapshot> {

        private final SingleObserver<? super UploadTask.TaskSnapshot> observer;

        Listener(@NonNull SingleObserver<? super UploadTask.TaskSnapshot> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
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
