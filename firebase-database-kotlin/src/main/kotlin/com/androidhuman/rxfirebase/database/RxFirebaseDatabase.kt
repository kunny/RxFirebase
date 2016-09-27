@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.rxfirebase.database

import com.androidhuman.rxfirebase.common.model.TaskResult
import com.google.firebase.database.*
import com.memoizrlabs.retrooptional.Optional
import rx.Observable

inline fun DatabaseReference.data()
        : Observable<DataSnapshot>
        = RxFirebaseDatabase.data(this)

inline fun DatabaseReference.dataChanges()
        : Observable<DataSnapshot>
        = RxFirebaseDatabase.dataChanges(this)

inline fun <reified T : Any> DatabaseReference.dataChangesOf()
        : Observable<Optional<T>>
        = RxFirebaseDatabase.dataChangesOf(this, T::class.java)

inline fun <reified T : Any> DatabaseReference.dataChangesOf(typeIndicator: GenericTypeIndicator<T>)
        : Observable<Optional<T>>
        = RxFirebaseDatabase.dataChangesOf(this, typeIndicator)

inline fun <reified T : Any> DatabaseReference.dataOf()
        : Observable<Optional<T>>
        = RxFirebaseDatabase.dataOf(this, T::class.java)

inline fun <reified T : Any> DatabaseReference.dataOf(typeIndicator: GenericTypeIndicator<T>)
        : Observable<Optional<T>>
        = RxFirebaseDatabase.dataOf(this, typeIndicator)

inline fun DatabaseReference.rxRunTransaction(noinline task: (MutableData) -> Transaction.Result)
        : Observable<TaskResult>
        = RxFirebaseDatabase.runTransaction(this, task)

inline fun DatabaseReference.rxRunTransaction(
        fireLocalEvents: Boolean, noinline task: (MutableData) -> Transaction.Result)
        : Observable<TaskResult>
        = RxFirebaseDatabase.runTransaction(this, fireLocalEvents, task)