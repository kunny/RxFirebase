@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.firebase.auth

import com.androidhuman.firebase.auth.model.TaskResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import rx.Observable

inline fun FirebaseAuth.rxAuthStateChanges()
        : Observable<FirebaseAuth>
        = RxFirebaseAuth.authStateChanges(this)

inline fun FirebaseAuth.rxCreateUserWithEmailAndPassword(email: String, password: String)
        : Observable<FirebaseUser?>
        = RxFirebaseAuth.createUserWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxFetchProvidersForEmail(email: String)
        : Observable<List<String>>
        = RxFirebaseAuth.fetchProvidersForEmail(this, email)
        .map { if (it.isPresent) it.get() else emptyList() }

inline fun FirebaseAuth.rxGetCurrentUser()
        : Observable<FirebaseUser?>
        = RxFirebaseAuth.getCurrentUser(this)
        .map { if (it.isPresent) it.get() else null }

inline fun FirebaseAuth.rxSendPasswordResetEmail(email: String)
        : Observable<TaskResult>
        = RxFirebaseAuth.sendPasswordResetEmail(this, email)

inline fun FirebaseAuth.rxSignInAnonymous()
        : Observable<FirebaseUser>
        = RxFirebaseAuth.signInAnonymous(this)

inline fun FirebaseAuth.rxSignInWithCredential(credential: AuthCredential)
        : Observable<FirebaseUser>
        = RxFirebaseAuth.signInWithCredential(this, credential)

inline fun FirebaseAuth.rxSignInWithCustomToken(token: String)
        : Observable<FirebaseUser>
        = RxFirebaseAuth.signInWithCustomToken(this, token)

inline fun FirebaseAuth.rxSignInWithEmailAndPassword(email: String, password: String)
        : Observable<FirebaseUser>
        = RxFirebaseAuth.signInWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxSignOut()
        : Observable<TaskResult>
        = RxFirebaseAuth.signOut(this)

