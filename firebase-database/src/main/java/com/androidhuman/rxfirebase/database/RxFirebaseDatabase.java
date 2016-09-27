package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase.common.model.TaskResult;
import com.androidhuman.rxfirebase.database.transformers.TransformerOfClazz;
import com.androidhuman.rxfirebase.database.transformers.TransformerOfGenericTypeIndicator;
import com.memoizrlabs.retrooptional.Optional;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Func1;

public final class RxFirebaseDatabase {

    @NonNull
    @CheckResult
    public static Observable<DataSnapshot> data(@NonNull DatabaseReference ref) {
        return Observable.create(new DataOnSubscribe(ref));
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
        return dataChanges(ref).compose(new TransformerOfClazz<T>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<Optional<T>> dataChangesOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return dataChanges(ref).compose(new TransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<Optional<T>> dataOf(
            @NonNull DatabaseReference ref, @NonNull Class<T> clazz) {
        return data(ref).compose(new TransformerOfClazz<T>(clazz));
    }

    @NonNull
    @CheckResult
    public static <T> Observable<Optional<T>> dataOf(
            @NonNull DatabaseReference ref, @NonNull GenericTypeIndicator<T> typeIndicator) {
        return data(ref).compose(new TransformerOfGenericTypeIndicator<T>(typeIndicator));
    }

    @NonNull
    @CheckResult
    public static Observable<TaskResult> runTransaction(
            @NonNull DatabaseReference ref, @NonNull Func1<MutableData, Transaction.Result> task) {
        return runTransaction(ref, true, task);
    }

    @NonNull
    @CheckResult
    public static Observable<TaskResult> runTransaction(
            @NonNull DatabaseReference ref, boolean fireLocalEvents,
            @NonNull Func1<MutableData, Transaction.Result> task) {
        return Observable.create(new RunTransactionOnSubscribe(ref, fireLocalEvents, task));
    }
}
