@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.androidhuman.rxfirebase2.firestore

import com.google.firebase.firestore.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

inline fun <reified T : Any> CollectionReference.rxAdd(value: T)
        : Single<DocumentReference> = RxFirebaseFirestore.addDocument(this, value)

inline fun CollectionReference.rxAdd(value: Map<String, Any>)
        : Single<DocumentReference> = RxFirebaseFirestore.addDocument(this, value)

inline fun <reified T : Any> DocumentReference.rxSet(value: T)
        : Completable = RxFirebaseFirestore.setDocument(this, value)

inline fun DocumentReference.rxSet(value: Map<String, Any>)
        : Completable = RxFirebaseFirestore.setDocument(this, value)

inline fun <reified T : Any> DocumentReference.rxSet(value: T, setOptions: SetOptions)
        : Completable = RxFirebaseFirestore.setDocument(this, value, setOptions)

inline fun DocumentReference.rxSet(value: Map<String, Any>, setOptions: SetOptions)
        : Completable = RxFirebaseFirestore.setDocument(this, value, setOptions)

inline fun DocumentReference.rxUpdate(value: Map<String, Any>)
        : Completable = RxFirebaseFirestore.updateDocument(this, value)

inline fun DocumentReference.rxRemove()
        : Completable = RxFirebaseFirestore.removeDocument(this)

inline fun DocumentReference.rxGet()
        : Single<DocumentSnapshot> = RxFirebaseFirestore.getDocument(this)

inline fun DocumentReference.rxGetChanges()
        : Observable<DocumentSnapshot> = RxFirebaseFirestore.getDocumentChanges(this)

inline fun Query.rxGet()
        : Single<QuerySnapshot> = RxFirebaseFirestore.query(this)

inline fun Query.rxGetChanges()
        : Observable<QuerySnapshot> = RxFirebaseFirestore.queryChanges(this)


