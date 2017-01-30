@file:Suppress("NOTHING_TO_INLINE")

package com.androidhuman.rxfirebase.auth

import com.androidhuman.rxfirebase.common.model.TaskResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Single

inline fun FirebaseUser.rxDelete()
        : Single<TaskResult>
        = RxFirebaseUser.delete(this)

inline fun FirebaseUser.rxGetToken(forceRefresh: Boolean)
        : Single<String>
        = RxFirebaseUser.getToken(this, forceRefresh)

inline fun FirebaseUser.rxLinkWithCredential(credential: AuthCredential)
        : Single<AuthResult>
        = RxFirebaseUser.linkWithCredential(this, credential)

inline fun FirebaseUser.rxReauthenticate(credential: AuthCredential)
        : Single<TaskResult>
        = RxFirebaseUser.reauthenticate(this, credential)

inline fun FirebaseUser.rxReload()
        : Single<TaskResult>
        = RxFirebaseUser.reload(this)

inline fun FirebaseUser.rxSendEmailVerification()
        : Single<TaskResult>
        = RxFirebaseUser.sendEmailVerification(this)

inline fun FirebaseUser.rxUnlink(provider: String)
        : Single<AuthResult>
        = RxFirebaseUser.unlink(this, provider)

inline fun FirebaseUser.rxUpdateEmail(email: String)
        : Single<TaskResult>
        = RxFirebaseUser.updateEmail(this, email)

inline fun FirebaseUser.rxUpdatePassword(password: String)
        : Single<TaskResult>
        = RxFirebaseUser.updatePassword(this, password)

inline fun FirebaseUser.rxUpdateProfile(request: UserProfileChangeRequest)
        : Single<TaskResult>
        = RxFirebaseUser.updateProfile(this, request)

