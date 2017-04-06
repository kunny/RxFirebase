package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase2.database.model.DataValue;
import com.androidhuman.rxfirebase2.database.transformers.SingleTransformerOfClazz;
import com.androidhuman.rxfirebase2.database.transformers.SingleTransformerOfGenericTypeIndicator;
import com.androidhuman.rxfirebase2.database.transformers.TransformerOfClazz;
import com.androidhuman.rxfirebase2.database.transformers.TransformerOfGenericTypeIndicator;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;


public final class RxFirebaseDatabase {

    @NonNull
    @CheckResult
    public static Observable<ChildEvent> childEvents(@NonNull DatabaseReference ref) {
        return Observable.create(new ChildEventsOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Observable<ChildEvent> childEvents(@NonNull Query query) {
        return Observable.create(new QueryChildEventsOnSubscribe(query));
    }

    @NonNull
    @CheckResult
    public static Single<DataSnapshot> data(@NonNull DatabaseReference ref) {
        return Single.create(new DataOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Single<DataSnapshot> data(@NonNull Query query) {
        return Single.create(new QueryOnSubscribe(query));
    }

    @NonNull
    @CheckResult
    public static Observable<DataSnapshot> dataChanges(@NonNull DatabaseReference ref) {
        return Observable.create(new DataChangesOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Observable<DataSnapshot> dataChanges(@NonNull Query query) {
        return Observable.create(new QueryChangesOnSubscribe(query));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return dataChanges(ref).compose(new TransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull Class<T> clazz) {
        return dataChanges(query).compose(new TransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(ref)
                .compose(new TransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(query)
                .compose(new TransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Single<T> dataOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return data(ref).compose(new SingleTransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Single<T> dataOf(
            @NonNull Query query, @NonNull Class<T> clazz) {
        return data(query).compose(new SingleTransformerOfClazz<T>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Single<T> dataOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return data(ref).compose(new SingleTransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Single<T> dataOf(
            @NonNull Query query, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return data(query).compose(new SingleTransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static Completable removeValue(@NonNull DatabaseReference ref) {
        return Completable.create(new RemoveValueOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Completable setPriority(
            @NonNull DatabaseReference ref, @NonNull Object priority) {
        return Completable.create(new SetPriorityOnSubscribe(ref, priority));
    }

    @NonNull
    @CheckResult
    public static <T> Completable setValue(
            @NonNull DatabaseReference ref, @NonNull T value) {
        return Completable.create(new SetValueOnSubscribe<T>(ref, value));
    }

    @NonNull
    @CheckResult
    public static <T> Completable setValue(
            @NonNull DatabaseReference ref, @NonNull T value, @NonNull Object priority) {
        return Completable.create(new SetValueWithPriorityOnSubscribe<T>(ref, value, priority));
    }

    @NonNull
    @CheckResult
    public static Completable runTransaction(
            @NonNull DatabaseReference ref,
            @NonNull Function<MutableData, Transaction.Result> task) {
        return runTransaction(ref, true, task);
    }

    @NonNull
    @CheckResult
    public static Completable runTransaction(
            @NonNull DatabaseReference ref, boolean fireLocalEvents,
            @NonNull Function<MutableData, Transaction.Result> task) {
        return Completable.create(new RunTransactionOnSubscribe(ref, fireLocalEvents, task));
    }

    @NonNull
    @CheckResult
    public static Completable updateChildren(
            @NonNull DatabaseReference ref, @NonNull Map<String, Object> update) {
        return Completable.create(new UpdateChildrenOnSubscribe(ref, update));
    }

    private RxFirebaseDatabase() {
        throw new AssertionError("No instances");
    }
}
