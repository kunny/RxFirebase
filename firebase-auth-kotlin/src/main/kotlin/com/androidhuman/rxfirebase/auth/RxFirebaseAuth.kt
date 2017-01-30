@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.rxfirebase.auth

import com.androidhuman.rxfirebase.common.model.TaskResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.memoizrlabs.retrooptional.Optional
import io.reactivex.Observable
import io.reactivex.Single

inline fun FirebaseAuth.authStateChanges()
        : Observable<FirebaseAuth>
        = RxFirebaseAuth.authStateChanges(this)

inline fun FirebaseAuth.rxCreateUserWithEmailAndPassword(email: String, password: String)
        : Single<FirebaseUser>
        = RxFirebaseAuth.createUserWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxFetchProvidersForEmail(email: String)
        : Single<List<String>>
        = RxFirebaseAuth.fetchProvidersForEmail(this, email)
        .map { if (it.isPresent) it.get() else emptyList() }

inline fun FirebaseAuth.rxGetCurrentUser()
        : Single<Optional<FirebaseUser>>
        = RxFirebaseAuth.getCurrentUser(this)

inline fun FirebaseAuth.rxSendPasswordResetEmail(email: String)
        : Single<TaskResult>
        = RxFirebaseAuth.sendPasswordResetEmail(this, email)

inline fun FirebaseAuth.rxSignInAnonymous()
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInAnonymous(this)

inline fun FirebaseAuth.rxSignInWithCredential(credential: AuthCredential)
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInWithCredential(this, credential)

inline fun FirebaseAuth.rxSignInWithCustomToken(token: String)
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInWithCustomToken(this, token)

inline fun FirebaseAuth.rxSignInWithEmailAndPassword(email: String, password: String)
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxSignOut()
        : Single<TaskResult>
        = RxFirebaseAuth.signOut(this)