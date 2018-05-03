package com.androidhuman.rxfirebase2.storage;

import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.Single;

public final class RxFirebaseStorage {

    private RxFirebaseStorage() {
        throw new AssertionError("No instances");
    }

    @NonNull
    @CheckResult
    public static Single<UploadTask.TaskSnapshot> putDocument(@NonNull StorageReference ref, @NonNull Uri uri) {
        return new PutFileObserver(ref, uri);
    }
}
