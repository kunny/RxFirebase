@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.rxfirebase.database

import com.androidhuman.rxfirebase.common.model.TaskResult
import com.google.firebase.database.*
import rx.Observable

inline fun DatabaseReference.childEvents()
        : Observable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this)

inline fun DatabaseReference.data()
        : Observable<DataSnapshot?>
        = RxFirebaseDatabase.data(this)
        .map { if (it.isPresent) it.get() else null }

inline fun DatabaseReference.dataChanges()
        : Observable<DataSnapshot?>
        = RxFirebaseDatabase.dataChanges(this)
        .map { if (it.isPresent) it.get() else null }

inline fun <reified T : Any> DatabaseReference.dataChangesOf()
        : Observable<T?>
        = RxFirebaseDatabase.dataChangesOf(this, T::class.java)
        .map { if (it.isPresent) it.get() else null }

inline fun <reified T : Any> DatabaseReference.dataChangesOf(typeIndicator: GenericTypeIndicator<T>)
        : Observable<T?>
        = RxFirebaseDatabase.dataChangesOf(this, typeIndicator)
        .map { if (it.isPresent) it.get() else null }

inline fun <reified T : Any> DatabaseReference.dataOf()
        : Observable<T?>
        = RxFirebaseDatabase.dataOf(this, T::class.java)
        .map { if (it.isPresent) it.get() else null }

inline fun <reified T : Any> DatabaseReference.dataOf(typeIndicator: GenericTypeIndicator<T>)
        : Observable<T?>
        = RxFirebaseDatabase.dataOf(this, typeIndicator)
        .map { if (it.isPresent) it.get() else null }

inline fun DatabaseReference.rxSetPriority(priority: Any)
        : Observable<TaskResult>
        = RxFirebaseDatabase.setPriority(this, priority)

inline fun <reified T : Any> DatabaseReference.rxSetValue(value: T)
        : Observable<TaskResult>
        = RxFirebaseDatabase.setValue(this, value)

inline fun <reified T : Any> DatabaseReference.rxSetValue(value: T, priority: Any)
        : Observable<TaskResult>
        = RxFirebaseDatabase.setValue(this, value, priority)

inline fun DatabaseReference.rxChildEvents()
        : Observable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this)

inline fun DatabaseReference.rxRunTransaction(noinline task: (MutableData) -> Transaction.Result)
        : Observable<TaskResult>
        = RxFirebaseDatabase.runTransaction(this, task)

inline fun DatabaseReference.rxRunTransaction(
        fireLocalEvents: Boolean, noinline task: (MutableData) -> Transaction.Result)
        : Observable<TaskResult>
        = RxFirebaseDatabase.runTransaction(this, fireLocalEvents, task)

inline fun DatabaseReference.rxUpdateChildren(update: Map<String, Any?>)
        : Observable<TaskResult>
        = RxFirebaseDatabase.updateChildren(this, update)