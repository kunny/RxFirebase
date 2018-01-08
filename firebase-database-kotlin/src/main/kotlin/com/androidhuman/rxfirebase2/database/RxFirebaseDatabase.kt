@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.androidhuman.rxfirebase2.database

import com.androidhuman.rxfirebase2.database.model.DataValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.MutableData
import com.google.firebase.database.Query
import com.google.firebase.database.Transaction
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

inline fun DatabaseReference.childEvents()
        : Observable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this)

inline fun DatabaseReference.chileEvents(strategy: BackpressureStrategy)
        : Flowable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this, strategy)

inline fun DatabaseReference.data()
        : Single<DataSnapshot>
        = RxFirebaseDatabase.data(this)

inline fun DatabaseReference.dataChanges()
        : Observable<DataSnapshot>
        = RxFirebaseDatabase.dataChanges(this)

inline fun DatabaseReference.dataChanges(strategy: BackpressureStrategy)
        : Flowable<DataSnapshot>
        = RxFirebaseDatabase.dataChanges(this, strategy)

inline fun <reified T : Any> DatabaseReference.dataChangesOf()
        : Observable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, T::class.java)

inline fun <reified T : Any> DatabaseReference.dataChangesOf(strategy: BackpressureStrategy)
        : Flowable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, T::class.java, strategy)

inline fun <reified T : Any> DatabaseReference.dataChangesOf(typeIndicator: GenericTypeIndicator<T>)
        : Observable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, typeIndicator)

inline fun <reified T : Any> DatabaseReference.dataChangesOf(
        typeIndicator: GenericTypeIndicator<T>, strategy: BackpressureStrategy)
        : Flowable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, typeIndicator, strategy)

inline fun <reified T : Any> DatabaseReference.dataOf()
        : Single<T>
        = RxFirebaseDatabase.dataOf(this, T::class.java)

inline fun <reified T : Any> DatabaseReference.dataOf(typeIndicator: GenericTypeIndicator<T>)
        : Single<T>
        = RxFirebaseDatabase.dataOf(this, typeIndicator)

inline fun DatabaseReference.rxRemoveValue()
        : Completable
        = RxFirebaseDatabase.removeValue(this)

inline fun DatabaseReference.rxRunTransaction(noinline task: (MutableData) -> Transaction.Result)
        : Completable
        = RxFirebaseDatabase.runTransaction(this, task)

inline fun DatabaseReference.rxRunTransaction(
        fireLocalEvents: Boolean, noinline task: (MutableData) -> Transaction.Result)
        : Completable
        = RxFirebaseDatabase.runTransaction(this, fireLocalEvents, task)

inline fun <reified T : Any> DatabaseReference.rxSetPriority(priority: T)
        : Completable
        = RxFirebaseDatabase.setPriority(this, priority)

inline fun <reified T : Any> DatabaseReference.rxSetValue(value: T)
        : Completable
        = RxFirebaseDatabase.setValue(this, value)

inline fun <reified T : Any, reified U : Any> DatabaseReference.rxSetValue(value: T, priority: U)
        : Completable
        = RxFirebaseDatabase.setValue(this, value, priority)

inline fun DatabaseReference.rxUpdateChildren(update: Map<String, Any?>)
        : Completable
        = RxFirebaseDatabase.updateChildren(this, update)

inline fun Query.childEvents()
        : Observable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this)

inline fun Query.childEvents(strategy: BackpressureStrategy)
        : Flowable<ChildEvent>
        = RxFirebaseDatabase.childEvents(this, strategy)

inline fun Query.data()
        : Single<DataSnapshot>
        = RxFirebaseDatabase.data(this)

inline fun Query.dataChanges()
        : Observable<DataSnapshot>
        = RxFirebaseDatabase.dataChanges(this)

inline fun Query.dataChanges(strategy: BackpressureStrategy)
        : Flowable<DataSnapshot>
        = RxFirebaseDatabase.dataChanges(this, strategy)

inline fun <reified T : Any> Query.dataChangesOf()
        : Observable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, T::class.java)

inline fun <reified T : Any> Query.dataChangesOf(strategy: BackpressureStrategy)
        : Flowable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, T::class.java, strategy)

inline fun <reified T : Any> Query.dataChangesOf(typeIndicator: GenericTypeIndicator<T>)
        : Observable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, typeIndicator)

inline fun <reified T : Any> Query.dataChangesOf(
        typeIndicator: GenericTypeIndicator<T>, strategy: BackpressureStrategy)
        : Flowable<DataValue<T>>
        = RxFirebaseDatabase.dataChangesOf(this, typeIndicator, strategy)

inline fun <reified T : Any> Query.dataOf()
        : Single<T>
        = RxFirebaseDatabase.dataOf(this, T::class.java)

inline fun <reified T : Any> Query.dataOf(typeIndicator: GenericTypeIndicator<T>)
        : Single<T>
        = RxFirebaseDatabase.dataOf(this, typeIndicator)
