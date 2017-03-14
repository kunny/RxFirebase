@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.rxfirebase.database

import com.androidhuman.rxfirebase.database.model.DataValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import rx.Completable
import rx.Observable
import rx.Single

inline fun DatabaseReference.childEvents()
        : Observable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this)

inline fun DatabaseReference.data()
        : Single<DataSnapshot>
        = RxFirebaseDatabase.data(this)

inline fun DatabaseReference.dataChanges()
        : Observable<DataSnapshot>
        = RxFirebaseDatabase.dataChanges(this)

inline fun <reified T : Any> DatabaseReference.dataChangesOf()
        : Observable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, T::class.java)

inline fun <reified T : Any> DatabaseReference.dataChangesOf(typeIndicator: GenericTypeIndicator<T>)
        : Observable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, typeIndicator)

inline fun <reified T : Any> DatabaseReference.dataOf()
        : Single<DataValue<T>>
        = RxFirebaseDatabase.dataOf(this, T::class.java)

inline fun <reified T : Any> DatabaseReference.dataOf(typeIndicator: GenericTypeIndicator<T>)
        : Single<DataValue<T>>
        = RxFirebaseDatabase.dataOf(this, typeIndicator)

inline fun DatabaseReference.rxSetPriority(priority: Any)
        : Completable
        = RxFirebaseDatabase.setPriority(this, priority)

inline fun <reified T : Any> DatabaseReference.rxSetValue(value: T)
        : Completable
        = RxFirebaseDatabase.setValue(this, value)

inline fun <reified T : Any> DatabaseReference.rxSetValue(value: T, priority: Any)
        : Completable
        = RxFirebaseDatabase.setValue(this, value, priority)

inline fun DatabaseReference.rxChildEvents()
        : Observable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this)

inline fun DatabaseReference.rxRunTransaction(noinline task: (MutableData) -> Transaction.Result)
        : Completable
        = RxFirebaseDatabase.runTransaction(this, task)

inline fun DatabaseReference.rxRunTransaction(
        fireLocalEvents: Boolean, noinline task: (MutableData) -> Transaction.Result)
        : Completable
        = RxFirebaseDatabase.runTransaction(this, fireLocalEvents, task)

inline fun DatabaseReference.rxUpdateChildren(update: Map<String, Any?>)
        : Completable
        = RxFirebaseDatabase.updateChildren(this, update)