package com.androidhuman.rxfirebase3.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@Suppress("NOTHING_TO_INLINE")
class RxPhoneAuthProviderTest {

    @Mock
    private lateinit var phoneAuthProvider: PhoneAuthProvider

    @Mock
    private lateinit var activity: Activity

    @Mock
    private lateinit var executor: Executor

    private val onVerificationStateChangedListener
            = argumentCaptor<PhoneAuthProvider.OnVerificationStateChangedCallbacks>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun verifyPhoneNumberActivityComplete() {
        val credential = mock<PhoneAuthCredential>()
        val forceResendingToken = mock<PhoneAuthProvider.ForceResendingToken>()

        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, activity)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity, OnVerificationStateChangedCallbacks) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(activity),
                            onVerificationStateChangedListener.capture())

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onCodeSent("123456", forceResendingToken)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) {
                it is PhoneAuthCodeSentEvent
                && "123456" == it.verificationId()
                && forceResendingToken == it.forceResendingToken()
            }

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onVerificationCompleted(credential)

            assertNoErrors()
            assertComplete()
            assertValueAt(1) {
                it is PhoneAuthVerificationCompleteEvent
                && credential == it.credential()
            }

            dispose()
        }
    }

    @Test
    fun verifyPhoneNumberActivityCodeAutoRetrievalTimeOut() {
        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, activity)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity, OnVerificationStateChangedCallbacks) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(activity),
                            onVerificationStateChangedListener.capture())

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onCodeAutoRetrievalTimeOut("123456")

            assertNoErrors()
            assertComplete()
            assertValue {
                it is PhoneAuthCodeAutoRetrievalTimeOutEvent
                        && "123456" == it.verificationId()
            }

            dispose()
        }
    }

    @Test
    fun verifyPhoneNumberActivityFailed() {
        val exception = mock<FirebaseException>()

        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, activity)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity, OnVerificationStateChangedCallbacks) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(activity),
                            onVerificationStateChangedListener.capture())

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onVerificationFailed(exception)

            assertError { it is FirebaseException }

            dispose()
        }
    }

    @Test
    fun verifyPhoneNumberActivityWithForceResendingToken() {
        val forceResendingToken = mock<PhoneAuthProvider.ForceResendingToken>()

        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, activity, forceResendingToken)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity,
            //   OnVerificationStateChangedCallbacks, ForceResendingToken) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(activity),
                            onVerificationStateChangedListener.capture(), eq(forceResendingToken))

            // do not run any other tests
            // since it uses PhoneAuthProviderVerifyPhoneNumberActivityObserver
            // which is being verified on verifyPhoneNumberActivity*() tests.
            dispose()
        }
    }

    @Test
    fun verifyPhoneNumberExecutorComplete() {
        val credential = mock<PhoneAuthCredential>()
        val forceResendingToken = mock<PhoneAuthProvider.ForceResendingToken>()

        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, executor)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity, OnVerificationStateChangedCallbacks) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(executor),
                            onVerificationStateChangedListener.capture())

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onCodeSent("123456", forceResendingToken)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) {
                it is PhoneAuthCodeSentEvent
                        && "123456" == it.verificationId()
                        && forceResendingToken == it.forceResendingToken()
            }

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onVerificationCompleted(credential)

            assertNoErrors()
            assertComplete()
            assertValueAt(1) {
                it is PhoneAuthVerificationCompleteEvent
                        && credential == it.credential()
            }

            dispose()
        }
    }

    @Test
    fun verifyPhoneNumberExecutorCodeAutoRetrievalTimeOut() {
        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, executor)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity, OnVerificationStateChangedCallbacks) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(executor),
                            onVerificationStateChangedListener.capture())

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onCodeAutoRetrievalTimeOut("123456")

            assertNoErrors()
            assertComplete()
            assertValue {
                it is PhoneAuthCodeAutoRetrievalTimeOutEvent
                        && "123456" == it.verificationId()
            }

            dispose()
        }
    }

    @Test
    fun verifyPhoneNumberExecutorFailed() {
        val exception = mock<FirebaseException>()

        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, executor)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity, OnVerificationStateChangedCallbacks) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(executor),
                            onVerificationStateChangedListener.capture())

            // simulate the callback
            onVerificationStateChangedListener.lastValue
                    .onVerificationFailed(exception)

            assertError { it is FirebaseException }

            dispose()
        }
    }

    @Test
    fun verifyPhoneNumberExecutorWithForceResendingToken() {
        val forceResendingToken = mock<PhoneAuthProvider.ForceResendingToken>()

        with(TestObserver.create<PhoneAuthEvent>()) {
            RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider,
                    "123-456-7890",
                    2L, TimeUnit.MINUTES, executor, forceResendingToken)
                    .subscribe(this)

            // verify verifyPhoneNumber(
            //   String, Long, TimeUnit, Activity,
            //   OnVerificationStateChangedCallbacks, ForceResendingToken) has called
            verify(phoneAuthProvider, times(1))
                    .verifyPhoneNumber(eq("123-456-7890"),
                            eq(2L), eq(TimeUnit.MINUTES), eq(executor),
                            onVerificationStateChangedListener.capture(), eq(forceResendingToken))

            // do not run any other tests
            // since it uses PhoneAuthProviderVerifyPhoneNumberActivityObserver
            // which is being verified on verifyPhoneNumberActivity*() tests.
            dispose()
        }
    }
}
