@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.androidhuman.rxfirebase2.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

inline fun FirebaseAuth.authStateChanges()
        : Observable<FirebaseAuth>
        = RxFirebaseAuth.authStateChanges(this)

inline fun FirebaseAuth.rxCreateUserWithEmailAndPassword(email: String, password: String)
        : Single<FirebaseUser>
        = RxFirebaseAuth.createUserWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxFetchSignInMethodsForEmail(email: String)
        : Maybe<List<String>>
        = RxFirebaseAuth.fetchSignInMethodsForEmail(this, email)

inline fun FirebaseAuth.rxGetCurrentUser()
        : Maybe<FirebaseUser>
        = RxFirebaseAuth.getCurrentUser(this)

inline fun FirebaseAuth.rxSendPasswordResetEmail(email: String)
        : Completable
        = RxFirebaseAuth.sendPasswordResetEmail(this, email)

inline fun FirebaseAuth.rxSignInAnonymously()
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInAnonymously(this)

inline fun FirebaseAuth.rxSignInWithCredential(credential: AuthCredential)
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInWithCredential(this, credential)

inline fun FirebaseAuth.rxSignInWithCustomToken(token: String)
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInWithCustomToken(this, token)

inline fun FirebaseAuth.rxSignInWithEmailAndPassword(email: String, password: String)
        : Single<AuthResult>
        = RxFirebaseAuth.signInWithEmailAndPasswordAuthResult(this, email, password)

inline fun FirebaseAuth.rxSignInAnonymouslyAuthResult()
        : Single<AuthResult>
        = RxFirebaseAuth.signInAnonymouslyAuthResult(this)

inline fun FirebaseAuth.rxSignInWithCredentialAuthResult(credential: AuthCredential)
        : Single<AuthResult>
        = RxFirebaseAuth.signInWithCredentialAuthResult(this, credential)

inline fun FirebaseAuth.rxSignInWithCustomTokenAuthResult(token: String)
        : Single<AuthResult>
        = RxFirebaseAuth.signInWithCustomTokenAuthResult(this, token)

inline fun FirebaseAuth.rxSignInWithEmailAndPasswordAuthResult(email: String, password: String)
        : Single<FirebaseUser>
        = RxFirebaseAuth.signInWithEmailAndPassword(this, email, password)

inline fun FirebaseAuth.rxSignInWithEmailLink(email: String, emailLink: String)
        : Single<AuthResult>
        = RxFirebaseAuth.signInWithEmailLink(this, email, emailLink)

inline fun FirebaseAuth.rxUpdateCurrentUser(user: FirebaseUser)
        : Completable
        = RxFirebaseAuth.updateCurrentUser(this, user)

inline fun FirebaseAuth.rxSignOut()
        : Completable
        = RxFirebaseAuth.signOut(this)