@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.androidhuman.rxfirebase2.auth

import android.app.Activity
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

inline fun PhoneAuthProvider.rxVerifyPhoneNumber(phoneNumber: String,
        timeout: Long, timeUnit: TimeUnit, activity: Activity)
        : Observable<PhoneAuthEvent>
        = RxPhoneAuthProvider.verifyPhoneNumber(
        this, phoneNumber, timeout, timeUnit, activity)

inline fun PhoneAuthProvider.rxVerifyPhoneNumber(phoneNumber: String,
        timeout: Long, timeUnit: TimeUnit, activity: Activity,
        forceResendingToken: PhoneAuthProvider.ForceResendingToken)
        : Observable<PhoneAuthEvent>
        = RxPhoneAuthProvider.verifyPhoneNumber(
        this, phoneNumber, timeout, timeUnit, activity, forceResendingToken)

inline fun PhoneAuthProvider.rxVerifyPhoneNumber(phoneNumber: String,
        timeout: Long, timeUnit: TimeUnit, executor: Executor)
        : Observable<PhoneAuthEvent>
        = RxPhoneAuthProvider.verifyPhoneNumber(
        this, phoneNumber, timeout, timeUnit, executor)

inline fun PhoneAuthProvider.rxVerifyPhoneNumber(phoneNumber: String,
        timeout: Long, timeUnit: TimeUnit, executor: Executor,
        forceResendingToken: PhoneAuthProvider.ForceResendingToken)
        : Observable<PhoneAuthEvent>
        = RxPhoneAuthProvider.verifyPhoneNumber(
        this, phoneNumber, timeout, timeUnit, executor, forceResendingToken)
