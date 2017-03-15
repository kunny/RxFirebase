package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase.database.model.DataValue;
import com.androidhuman.rxfirebase.database.transformers.SingleTransformerOfClazz;
import com.androidhuman.rxfirebase.database.transformers.SingleTransformerOfGenericTypeIndicator;
import com.androidhuman.rxfirebase.database.transformers.TransformerOfClazz;
import com.androidhuman.rxfirebase.database.transformers.TransformerOfGenericTypeIndicator;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

public final class RxFirebaseDatabase {

    @NonNull
    @CheckResult
    public static Observable<ChildEvent> childEvents(@NonNull DatabaseReference ref) {
        //noinspection deprecation
        return Observable.create(new ChildEventsOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Observable<ChildEvent> childEvents(@NonNull Query query) {
        //noinspection deprecation
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
        //noinspection deprecation
        return Observable.create(new DataChangesOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Observable<DataSnapshot> dataChanges(@NonNull Query query) {
        //noinspection deprecation
        return Observable.create(new QueryChangesOnSubscribe(query));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return dataChanges(ref).compose(new TransformerOfClazz<T>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull Class<T> clazz) {
        return dataChanges(query).compose(new TransformerOfClazz<T>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(ref).compose(new TransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<DataValue<T>> dataChangesOf(
            @NonNull Query query, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(query).compose(new TransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Single<DataValue<T>> dataOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return data(ref).compose(new SingleTransformerOfClazz<T>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Single<DataValue<T>> dataOf(
            @NonNull Query query, @NonNull Class<T> clazz) {
        return data(query).compose(new SingleTransformerOfClazz<T>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Single<DataValue<T>> dataOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return data(ref).compose(new SingleTransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Single<DataValue<T>> dataOf(
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
            @NonNull DatabaseReference ref, @Nullable T value) {
        return Completable.create(new SetValueOnSubscribe<T>(ref, value));
    }

    @NonNull
    @CheckResult
    public static <T> Completable setValue(
            @NonNull DatabaseReference ref, @Nullable T value, @NonNull Object priority) {
        return Completable.create(new SetValueWithPriorityOnSubscribe<T>(ref, value, priority));
    }

    @NonNull
    @CheckResult
    public static Completable runTransaction(
            @NonNull DatabaseReference ref, @NonNull Func1<MutableData, Transaction.Result> task) {
        return runTransaction(ref, true, task);
    }

    @NonNull
    @CheckResult
    public static Completable runTransaction(
            @NonNull DatabaseReference ref, boolean fireLocalEvents,
            @NonNull Func1<MutableData, Transaction.Result> task) {
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
