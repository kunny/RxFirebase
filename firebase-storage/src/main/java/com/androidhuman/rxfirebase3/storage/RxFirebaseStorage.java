package com.androidhuman.rxfirebase3.storage;

import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.rxjava3.core.Single;

public final class RxFirebaseStorage {

    @NonNull
    @CheckResult
    public static Single<UploadTask.TaskSnapshot> putDocument(@NonNull StorageReference ref,
                                                              @NonNull Uri uri) {
        return new PutFileObserver(ref, uri);
    }

    @NonNull
    @CheckResult
    public static Single<UploadTask.TaskSnapshot> putDocument(@NonNull StorageReference ref,
            @NonNull Uri uri, @NonNull StorageMetadata metadata) {
        return new PutFileWithMetadataObserver(ref, uri, metadata);
    }

    @NonNull
    @CheckResult
    public static Single<Uri> downloadUrl(@NonNull StorageReference ref) {
        return new DownloadUrlObserver(ref);
    }

    private RxFirebaseStorage() {
        throw new AssertionError("No instances");
    }
}
