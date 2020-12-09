package com.androidhuman.rxfirebase3.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase3.database.model.DataValue;
import com.androidhuman.rxfirebase3.database.transformers.SingleTransformerOfClazz;
import com.androidhuman.rxfirebase3.database.transformers.SingleTransformerOfGenericTypeIndicator;
import com.androidhuman.rxfirebase3.database.transformers.TransformerOfClazz;
import com.androidhuman.rxfirebase3.database.transformers.TransformerOfGenericTypeIndicator;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;

public final class RxFirebaseDatabase {

    @NonNull
    @CheckResult
    public static Observable<ChildEvent> childEvents(@NonNull DatabaseReference ref) {
        return new ChildEventsObserver(ref);
    }

    @NonNull
    @CheckResult
    public static Flowable<ChildEvent> childEvents(
            @NonNull DatabaseReference ref, BackpressureStrategy strategy) {
        return childEvents(ref).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static Observable<ChildEvent> childEvents(@NonNull Query query) {
        return new QueryChildEventsObserver(query);
    }

    @NonNull
    @CheckResult
    public static Flowable<ChildEvent> childEvents(
            @NonNull Query query, BackpressureStrategy strategy) {
        return childEvents(query).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static Single<DataSnapshot> data(@NonNull DatabaseReference ref) {
        return new DataObserver(ref);
    }

    @NonNull
    @CheckResult
    public static Single<DataSnapshot> data(@NonNull Query query) {
        return new QueryObserver(query);
    }

    @NonNull
    @CheckResult
    public static Observable<DataSnapshot> dataChanges(@NonNull DatabaseReference ref) {
        return new DataChangesObserver(ref);
    }

    @NonNull
    @CheckResult
    public static Flowable<DataSnapshot> dataChanges(
            @NonNull DatabaseReference ref, BackpressureStrategy strategy) {
        return dataChanges(ref).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static Observable<DataSnapshot> dataChanges(@NonNull Query query) {
        return new QueryChangesObserver(query);
    }

    @NonNull
    @CheckResult
    public static Flowable<DataSnapshot> dataChanges(
            @NonNull Query query, BackpressureStrategy strategy) {
        return dataChanges(query).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return dataChanges(ref).compose(new TransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Flowable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz,
            BackpressureStrategy strategy) {
        return dataChangesOf(ref, clazz).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull Class<T> clazz) {
        return dataChanges(query).compose(new TransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Flowable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull Class<T> clazz,
            BackpressureStrategy strategy) {
        return dataChangesOf(query, clazz).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(ref)
                .compose(new TransformerOfGenericTypeIndicator<>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Flowable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator,
            BackpressureStrategy strategy) {
        return dataChangesOf(ref, typeIndicator).toFlowable(strategy);
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(query)
                .compose(new TransformerOfGenericTypeIndicator<>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Flowable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull GenericTypeIndicator<T> typeIndicator,
            BackpressureStrategy strategy) {
        return dataChangesOf(query, typeIndicator).toFlowable(strategy);
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
        return data(query).compose(new SingleTransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Single<T> dataOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return data(ref).compose(new SingleTransformerOfGenericTypeIndicator<>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Single<T> dataOf(
            @NonNull Query query, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return data(query).compose(new SingleTransformerOfGenericTypeIndicator<>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static Completable removeValue(@NonNull DatabaseReference ref) {
        return new RemoveValueObserver(ref);
    }

    @NonNull
    @CheckResult
    public static <T> Completable setPriority(
            @NonNull DatabaseReference ref, @NonNull T priority) {
        return new SetPriorityObserver<>(ref, priority);
    }

    @NonNull
    @CheckResult
    public static <T> Completable setValue(
            @NonNull DatabaseReference ref, @NonNull T value) {
        return new SetValueObserver<>(ref, value);
    }

    @NonNull
    @CheckResult
    public static <T, U> Completable setValue(
            @NonNull DatabaseReference ref, @NonNull T value, @NonNull U priority) {
        return new SetValueWithPriorityObserver<>(ref, value, priority);
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
        return new RunTransactionObserver(ref, fireLocalEvents, task);
    }

    @NonNull
    @CheckResult
    public static Completable updateChildren(
            @NonNull DatabaseReference ref, @NonNull Map<String, Object> update) {
        return new UpdateChildrenObserver(ref, update);
    }

    private RxFirebaseDatabase() {
        throw new AssertionError("No instances");
    }
}
