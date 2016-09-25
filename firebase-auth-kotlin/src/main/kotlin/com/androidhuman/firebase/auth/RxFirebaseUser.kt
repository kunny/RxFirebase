@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.firebase.auth

import com.androidhuman.firebase.auth.model.TaskResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import rx.Observable

inline fun FirebaseUser.rxDelete()
        : Observable<TaskResult>
        = RxFirebaseUser.delete(this)

inline fun FirebaseUser.rxGetToken(forceRefresh: Boolean)
        : Observable<String>
        = RxFirebaseUser.getToken(this, forceRefresh)

inline fun FirebaseUser.rxLinkWithCredential(credential: AuthCredential)
        : Observable<AuthResult>
        = RxFirebaseUser.linkWithCredential(this, credential)

inline fun FirebaseUser.rxReauthenticate(credential: AuthCredential)
        : Observable<TaskResult>
        = RxFirebaseUser.reauthenticate(this, credential)

inline fun FirebaseUser.rxReload()
        : Observable<TaskResult>
        = RxFirebaseUser.reload(this)

inline fun FirebaseUser.rxSendEmailVerification()
        : Observable<TaskResult>
        = RxFirebaseUser.sendEmailVerification(this)

inline fun FirebaseUser.rxUnlink(provider: String)
        : Observable<AuthResult>
        = RxFirebaseUser.unlink(this, provider)

inline fun FirebaseUser.rxUpdateEmail(email: String)
        : Observable<TaskResult>
        = RxFirebaseUser.updateEmail(this, email)

inline fun FirebaseUser.rxUpdatePassword(password: String)
        : Observable<TaskResult>
        = RxFirebaseUser.updatePassword(this, password)

inline fun FirebaseUser.rxUpdateProfile(request: UserProfileChangeRequest)
        : Observable<TaskResult>
        = RxFirebaseUser.updateProfile(this, request)

