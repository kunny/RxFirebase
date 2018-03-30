package com.androidhuman.rxfirebase2.firestore;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public final class RxFirebaseFirestore {

    private RxFirebaseFirestore() {
        throw new AssertionError("No instances");
    }

    @NonNull
    @CheckResult
    public static Single<DocumentSnapshot> getDocument(@NonNull DocumentReference ref) {
        return new DocumentObserver(ref);
    }

    @NonNull
    @CheckResult
    public static Observable<DocumentSnapshot> getDocumentChanges(@NonNull DocumentReference ref) {
        return new DocumentChangesObserver(ref);
    }

    @NonNull
    @CheckResult
    public static Single<QuerySnapshot> query(@NonNull Query query) {
        return new QueryObserver(query);
    }

    @NonNull
    @CheckResult
    public static Observable<QuerySnapshot> queryChanges(@NonNull Query query) {
        return new QueryChangesObserver(query);
    }

    @NonNull
    @CheckResult
    public static <T> Completable setDocument(@NonNull DocumentReference ref, @NonNull T value) {
        return new SetDocumentObserver<>(ref, value);
    }

    @NonNull
    @CheckResult
    public static Completable setDocument(@NonNull DocumentReference ref, @NonNull Map<String, Object> value) {
        return new SetMapDocumentObserver(ref, value);
    }

    @NonNull
    @CheckResult
    public static <T> Completable setDocument(@NonNull DocumentReference ref, @NonNull T value, @NonNull SetOptions setOptions) {
        return new SetDocumentWithSetOptionsObserver<>(ref, value, setOptions);
    }

    @NonNull
    @CheckResult
    public static Completable setDocument(@NonNull DocumentReference ref, @NonNull Map<String, Object> value, @NonNull SetOptions setOptions) {
        return new SetMapDocumentWithSetOptionsObserver(ref, value, setOptions);
    }

    @NonNull
    @CheckResult
    public static <T> Single<DocumentReference> addDocument(@NonNull CollectionReference ref, @NonNull T value) {
        return new AddDocumentObserver<>(ref, value);
    }

    @NonNull
    @CheckResult
    public static Single<DocumentReference> addDocument(@NonNull CollectionReference ref, @NonNull Map<String, Object> value) {
        return new AddMapDocumentObserver(ref, value);
    }

    @NonNull
    @CheckResult
    public static Completable updateDocument(@NonNull DocumentReference ref, @NonNull Map<String, Object> value) {
        return new UpdateMapDocumentObserver(ref, value);
    }

    @NonNull
    @CheckResult
    public static Completable removeDocument(@NonNull DocumentReference ref) {
        return new DeleteDocumentObserver(ref);
    }
}
