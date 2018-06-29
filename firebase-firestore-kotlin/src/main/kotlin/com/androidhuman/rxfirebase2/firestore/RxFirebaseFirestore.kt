@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.androidhuman.rxfirebase2.firestore

import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

inline fun <reified T : Any> CollectionReference.rxAdd(value: T)
        : Single<DocumentReference> = RxFirebaseFirestore.add(this, value)

inline fun CollectionReference.rxAdd(value: Map<String, Any>)
        : Single<DocumentReference> = RxFirebaseFirestore.add(this, value)

inline fun DocumentReference.data()
        : Single<Value<DocumentSnapshot>> = RxFirebaseFirestore.data(this)

inline fun DocumentReference.data(source: Source)
        : Single<Value<DocumentSnapshot>> = RxFirebaseFirestore.data(this, source)

inline fun Query.data()
        : Single<Value<QuerySnapshot>> = RxFirebaseFirestore.data(this)

inline fun Query.data(source: Source)
        : Single<Value<QuerySnapshot>> = RxFirebaseFirestore.data(this, source)

inline fun DocumentReference.dataChanges(strategy: BackpressureStrategy)
        : Flowable<Value<DocumentSnapshot>> = RxFirebaseFirestore.dataChanges(this, strategy)

inline fun DocumentReference.dataChanges()
        : Observable<Value<DocumentSnapshot>> = RxFirebaseFirestore.dataChanges(this)

inline fun Query.dataChanges(strategy: BackpressureStrategy)
        : Flowable<Value<QuerySnapshot>> = RxFirebaseFirestore.dataChanges(this, strategy)

inline fun Query.dataChanges()
        : Observable<Value<QuerySnapshot>> = RxFirebaseFirestore.dataChanges(this)

inline fun DocumentReference.rxDelete()
        : Completable = RxFirebaseFirestore.delete(this)

inline fun <reified T : Any> DocumentReference.rxSet(value: T)
        : Completable = RxFirebaseFirestore.set(this, value)

inline fun <reified T : Any> DocumentReference.rxSet(value: T, options: SetOptions)
        : Completable = RxFirebaseFirestore.set(this, value, options);

inline fun DocumentReference.rxSet(value: Map<String, Any>)
        : Completable = RxFirebaseFirestore.set(this, value)

inline fun DocumentReference.rxSet(value: Map<String, Any>, options: SetOptions)
        : Completable = RxFirebaseFirestore.set(this, value, options)

inline fun DocumentReference.rxUpdate(value: Map<String, Any>)
        : Completable = RxFirebaseFirestore.update(this, value)
