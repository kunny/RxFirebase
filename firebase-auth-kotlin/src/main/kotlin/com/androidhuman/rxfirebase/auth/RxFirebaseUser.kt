@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.rxfirebase.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable
import io.reactivex.Single

inline fun FirebaseUser.rxDelete()
        : Completable
        = RxFirebaseUser.delete(this)

inline fun FirebaseUser.rxGetToken(forceRefresh: Boolean)
        : Single<String>
        = RxFirebaseUser.getToken(this, forceRefresh)

inline fun FirebaseUser.rxLinkWithCredential(credential: AuthCredential)
        : Single<AuthResult>
        = RxFirebaseUser.linkWithCredential(this, credential)

inline fun FirebaseUser.rxReauthenticate(credential: AuthCredential)
        : Completable
        = RxFirebaseUser.reauthenticate(this, credential)

inline fun FirebaseUser.rxReload()
        : Completable
        = RxFirebaseUser.reload(this)

inline fun FirebaseUser.rxSendEmailVerification()
        : Completable
        = RxFirebaseUser.sendEmailVerification(this)

inline fun FirebaseUser.rxUnlink(provider: String)
        : Single<AuthResult>
        = RxFirebaseUser.unlink(this, provider)

inline fun FirebaseUser.rxUpdateEmail(email: String)
        : Completable
        = RxFirebaseUser.updateEmail(this, email)

inline fun FirebaseUser.rxUpdatePassword(password: String)
        : Completable
        = RxFirebaseUser.updatePassword(this, password)

inline fun FirebaseUser.rxUpdateProfile(request: UserProfileChangeRequest)
        : Completable
        = RxFirebaseUser.updateProfile(this, request)

