package com.androidhuman.rxfirebase2.storage;

import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.net.Uri;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import io.reactivex.Single;

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
