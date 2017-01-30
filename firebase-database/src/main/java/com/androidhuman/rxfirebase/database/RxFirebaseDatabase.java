package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase.common.model.TaskResult;
import com.androidhuman.rxfirebase.database.transformers.ObsTransformerOfClazz;
import com.androidhuman.rxfirebase.database.transformers.ObsTransformerOfGenericTypeIndicator;
import com.androidhuman.rxfirebase.database.transformers.SingleTransformerOfClazz;
import com.androidhuman.rxfirebase.database.transformers.SingleTransformerOfGenericTypeIndicator;
import com.memoizrlabs.retrooptional.Optional;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

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
    public static Single<DataSnapshot> data(@NonNull DatabaseReference ref) {
        return Single.create(new DataOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Observable<DataSnapshot> dataChanges(@NonNull DatabaseReference ref) {
        return Observable.create(new DataChangesOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<Optional<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return dataChanges(ref).compose(new ObsTransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<Optional<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(ref)
                .compose(new ObsTransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Single<Optional<T>> dataOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return data(ref).compose(new SingleTransformerOfClazz<>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Single<Optional<T>> dataOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return data(ref).compose(new SingleTransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static Single<TaskResult> removeValue(@NonNull DatabaseReference ref) {
        return Single.create(new RemoveValueOnSubscribe(ref));
    }

    @NonNull
    @CheckResult
    public static Single<TaskResult> setPriority(
            @NonNull DatabaseReference ref, @NonNull Object priority) {
        return Single.create(new SetPriorityOnSubscribe(ref, priority));
    }

    @NonNull
    @CheckResult
    public static <T> Single<TaskResult> setValue(
            @NonNull DatabaseReference ref, @Nullable T value) {
        return Single.create(new SetValueOnSubscribe<T>(ref, value));
    }

    @NonNull
    @CheckResult
    public static <T> Single<TaskResult> setValue(
            @NonNull DatabaseReference ref, @Nullable T value, @NonNull Object priority) {
        return Single.create(new SetValueWithPriorityOnSubscribe<T>(ref, value, priority));
    }

    @NonNull
    @CheckResult
    public static Single<TaskResult> runTransaction(
            @NonNull DatabaseReference ref,
            @NonNull Function<MutableData, Transaction.Result> task) {
        return runTransaction(ref, true, task);
    }

    @NonNull
    @CheckResult
    public static Single<TaskResult> runTransaction(
            @NonNull DatabaseReference ref, boolean fireLocalEvents,
            @NonNull Function<MutableData, Transaction.Result> task) {
        return Single.create(new RunTransactionOnSubscribe(ref, fireLocalEvents, task));
    }

    @NonNull
    @CheckResult
    public static Single<TaskResult> updateChildren(
            @NonNull DatabaseReference ref, @NonNull Map<String, Object> update) {
        return Single.create(new UpdateChildrenOnSubscribe(ref, update));
    }

    private RxFirebaseDatabase() {
        throw new AssertionError("No instances");
    }
}
