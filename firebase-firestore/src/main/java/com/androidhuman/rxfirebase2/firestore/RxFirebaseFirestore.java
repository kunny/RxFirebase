package com.androidhuman.rxfirebase2.firestore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import com.androidhuman.rxfirebase2.firestore.model.Value;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public final class RxFirebaseFirestore {

    @NonNull
    @CheckResult
    public static <T> Single<DocumentReference> add(
            @NonNull CollectionReference ref, @NonNull T value) {
        return new AddDocumentObserver<>(ref, value);
    }

    @NonNull
    @CheckResult
    public static Single<DocumentReference> add(
            @NonNull CollectionReference ref, @NonNull Map<String, Object> value) {
        return new AddMapDocumentObserver(ref, value);
    }

    @NonNull
    @CheckResult
    public static Single<Value<DocumentSnapshot>> data(@NonNull DocumentReference ref) {
        return new DocumentObserver(ref, null);
    }

    @NonNull
    @CheckResult
    public static Single<Value<DocumentSnapshot>> data(
            @NonNull DocumentReference ref, @NonNull Source source) {
        return new DocumentObserver(ref, source);
    }

    @NonNull
    @CheckResult
    public static Single<Value<QuerySnapshot>> data(@NonNull Query query) {
        return new QueryObserver(query);
    }

    @NonNull
    @CheckResult
    public static Single<Value<QuerySnapshot>> data(
            @NonNull Query query, @NonNull Source source) {
        return new QueryObserver(query, source);
    }

    @NonNull
    @CheckResult
    public static Flowable<Value<DocumentSnapshot>> dataChanges(
            @NonNull DocumentReference ref, @NonNull BackpressureStrategy strategy) {
        return dataChanges(ref).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static Observable<Value<DocumentSnapshot>> dataChanges(
            @NonNull DocumentReference ref) {
        return new DocumentChangesObserver(ref);
    }

    @NonNull
    @CheckResult
    public static Flowable<Value<QuerySnapshot>> dataChanges(
            @NonNull Query query, @NonNull BackpressureStrategy strategy) {
        return dataChanges(query).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static Observable<Value<QuerySnapshot>> dataChanges(@NonNull Query query) {
        return new QueryChangesObserver(query);
    }

    @NonNull
    @CheckResult
    public static Completable delete(@NonNull DocumentReference ref) {
        return new DeleteDocumentObserver(ref);
    }

    @NonNull
    @CheckResult
    public static <T> Completable set(@NonNull DocumentReference ref, @NonNull T value) {
        return new SetValueObserver<>(ref, value);
    }

    @NonNull
    @CheckResult
    public static <T> Completable set(
            @NonNull DocumentReference ref, @NonNull T value, @NonNull SetOptions options) {
        return new SetValueObserver<>(ref, value, options);
    }

    @NonNull
    @CheckResult
    public static Completable set(
            @NonNull DocumentReference ref, @NonNull Map<String, Object> value) {
        return new SetMapValueObserver(ref, value);
    }

    @NonNull
    @CheckResult
    public static Completable set(
            @NonNull DocumentReference ref,
            @NonNull Map<String, Object> value, @NonNull SetOptions options) {
        return new SetMapValueObserver(ref, value, options);
    }

    @NonNull
    @CheckResult
    public static Completable update(
            @NonNull DocumentReference ref, @NonNull Map<String, Object> value) {
        return new UpdateMapDocumentObserver(ref, value);
    }

    private RxFirebaseFirestore() {
        throw new AssertionError("No instances");
    }
}
