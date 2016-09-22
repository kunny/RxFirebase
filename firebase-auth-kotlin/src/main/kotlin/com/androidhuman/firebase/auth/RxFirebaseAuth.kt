@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.firebase.auth

import com.androidhuman.firebase.auth.model.OptionalFirebaseUser
import com.androidhuman.firebase.auth.model.TaskResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import rx.Observable

inline fun FirebaseAuth.rxAuthStateChanges()
        : Observable<FirebaseAuth>
        = RxFirebaseAuth.authStateChanges(this)

inline fun FirebaseAuth.rxCreateUserWithEmailAndPassword(email: String, password: String)
        : Observable<OptionalFirebaseUser>
        = RxFirebaseAuth.createUserWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxGetCurrentUser(): Observable<OptionalFirebaseUser>
        = RxFirebaseAuth.getCurrentUser(this)

inline fun FirebaseAuth.rxSignInAnonymous(): Observable<OptionalFirebaseUser>
        = RxFirebaseAuth.signInAnonymous(this)

inline fun FirebaseAuth.rxSignInWithCredential(credential: AuthCredential)
        : Observable<OptionalFirebaseUser>
        = RxFirebaseAuth.signInWithCredential(this, credential)

inline fun FirebaseAuth.rxSignInWithCustomToken(token: String)
        : Observable<OptionalFirebaseUser>
        = RxFirebaseAuth.signInWithCustomToken(this, token)

inline fun FirebaseAuth.rxSignInWithEmailAndPassword(email: String, password: String)
        : Observable<OptionalFirebaseUser>
        = RxFirebaseAuth.signInWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxSignOut()
        : Observable<TaskResult>
        = RxFirebaseAuth.signOut(this)
