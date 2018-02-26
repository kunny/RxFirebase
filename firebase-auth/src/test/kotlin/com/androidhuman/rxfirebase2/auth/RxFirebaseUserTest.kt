package com.androidhuman.rxfirebase2.auth

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@Suppress("NOTHING_TO_INLINE", "DEPRECATION")
class RxFirebaseUserTest {

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    private val onAuthResultCompleteListener
            = argumentCaptor<OnCompleteListener<AuthResult>>()

    private val onGetTokenResultCompleteListener
            = argumentCaptor<OnCompleteListener<GetTokenResult>>()

    private val onVoidCompleteListener
            = argumentCaptor<OnCompleteListener<Void>>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun delete() {
        val task = succeedVoidTask()

        whenever(firebaseUser.delete())
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.delete(firebaseUser)
                    .subscribe(this)

            // verify delete() has called
            verify(firebaseUser, times(1))
                    .delete()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun deleteNotSuccessful() {
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseUser.delete())
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.delete(firebaseUser)
                    .subscribe(this)

            // verify delete() has called
            verify(firebaseUser, times(1))
                    .delete()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun getToken() {
        val task = succeedGetTokenResultTask("token")

        whenever(firebaseUser.getToken(true))
                .thenReturn(task)

        with(TestObserver.create<String>()) {
            RxFirebaseUser.getToken(firebaseUser, true)
                    .subscribe(this)

            // verify delete() has called
            verify(firebaseUser, times(1))
                    .getToken(true)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onGetTokenResultCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun getTokenNotSuccessful() {
        val task = failedTask<GetTokenResult>(IllegalStateException())

        whenever(firebaseUser.getToken(true))
                .thenReturn(task)

        with(TestObserver.create<String>()) {
            RxFirebaseUser.getToken(firebaseUser, true)
                    .subscribe(this)

            // verify delete() has called
            verify(firebaseUser, times(1))
                    .getToken(true)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onGetTokenResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun getIdToken() {
        val task = succeedGetTokenResultTask("token")

        whenever(firebaseUser.getIdToken(true))
                .thenReturn(task)

        with(TestObserver.create<String>()) {
            RxFirebaseUser.getIdToken(firebaseUser, true)
                    .subscribe(this)

            // verify delete() has called
            verify(firebaseUser, times(1))
                    .getIdToken(true)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onGetTokenResultCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun getIdTokenNotSuccessful() {
        val task = failedTask<GetTokenResult>(IllegalStateException())

        whenever(firebaseUser.getIdToken(true))
                .thenReturn(task)

        with(TestObserver.create<String>()) {
            RxFirebaseUser.getIdToken(firebaseUser, true)
                    .subscribe(this)

            // verify delete() has called
            verify(firebaseUser, times(1))
                    .getIdToken(true)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onGetTokenResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun linkWithCredential() {
        val credential = mock<AuthCredential>()
        val task = succeedAuthResultTask("foo@bar.com")

        whenever(firebaseUser.linkWithCredential(credential))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseUser.linkWithCredential(firebaseUser, credential)
                    .subscribe(this)

            // verify linkWithCredential() has called
            verify(firebaseUser, times(1))
                    .linkWithCredential(credential)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun linkWithCredentialNotSuccessful() {
        val credential = mock<AuthCredential>()
        val task = failedTask<AuthResult>(IllegalStateException())

        whenever(firebaseUser.linkWithCredential(credential))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseUser.linkWithCredential(firebaseUser, credential)
                    .subscribe(this)

            // verify linkWithCredential() has called
            verify(firebaseUser, times(1))
                    .linkWithCredential(credential)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun reauthenticate() {
        val credential = mock<AuthCredential>()
        val task = succeedVoidTask()

        whenever(firebaseUser.reauthenticate(credential))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.reauthenticate(firebaseUser, credential)
                    .subscribe(this)

            // verify reauthenticate() has called
            verify(firebaseUser, times(1))
                    .reauthenticate(credential)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun reauthenticateNotSuccessful() {
        val credential = mock<AuthCredential>()
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseUser.reauthenticate(credential))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.reauthenticate(firebaseUser, credential)
                    .subscribe(this)

            // verify reauthenticate() has called
            verify(firebaseUser, times(1))
                    .reauthenticate(credential)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun reload() {
        val task = succeedVoidTask()

        whenever(firebaseUser.reload())
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.reload(firebaseUser)
                    .subscribe(this)

            // verify reload() has called
            verify(firebaseUser, times(1))
                    .reload()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun reloadNotSuccessful() {
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseUser.reload())
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.reload(firebaseUser)
                    .subscribe(this)

            // verify reload() has called
            verify(firebaseUser, times(1))
                    .reload()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun sendEmailVerification() {
        val task = succeedVoidTask()

        whenever(firebaseUser.sendEmailVerification())
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.sendEmailVerification(firebaseUser)
                    .subscribe(this)

            // verify sendEmailVerification() has called
            verify(firebaseUser, times(1))
                    .sendEmailVerification()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun sendEmailVerificationNotSuccessful() {
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseUser.sendEmailVerification())
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.sendEmailVerification(firebaseUser)
                    .subscribe(this)

            // verify sendEmailVerification() has called
            verify(firebaseUser, times(1))
                    .sendEmailVerification()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun unlink() {
        val task = succeedAuthResultTask("foo@bar.com")

        whenever(firebaseUser.unlink("provider"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseUser.unlink(firebaseUser, "provider")
                    .subscribe(this)

            // verify unlink() has called
            verify(firebaseUser, times(1))
                    .unlink("provider")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun unlinkNotSuccessful() {
        val task = failedTask<AuthResult>(IllegalStateException())

        whenever(firebaseUser.unlink("provider"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseUser.unlink(firebaseUser, "provider")
                    .subscribe(this)

            // verify unlink() has called
            verify(firebaseUser, times(1))
                    .unlink("provider")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun updateEmail() {
        val task = succeedVoidTask()

        whenever(firebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updateEmail(firebaseUser, "foo@bar.com")
                    .subscribe(this)

            // verify updateEmail() has called
            verify(firebaseUser, times(1))
                    .updateEmail("foo@bar.com")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun updateEmailNotSuccessful() {
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updateEmail(firebaseUser, "foo@bar.com")
                    .subscribe(this)

            // verify updateEmail() has called
            verify(firebaseUser, times(1))
                    .updateEmail("foo@bar.com")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun updatePassword() {
        val task = succeedVoidTask()

        whenever(firebaseUser.updatePassword("password"))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updatePassword(firebaseUser, "password")
                    .subscribe(this)

            // verify updatePassword() has called
            verify(firebaseUser, times(1))
                    .updatePassword("password")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun updatePasswordNotSuccessful() {
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseUser.updatePassword("password"))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updatePassword(firebaseUser, "password")
                    .subscribe(this)

            // verify updatePassword() has called
            verify(firebaseUser, times(1))
                    .updatePassword("password")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun updatePhoneNumber() {
        val task = succeedVoidTask()
        val credential = mock<PhoneAuthCredential>()

        whenever(firebaseUser.updatePhoneNumber(credential))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updatePhoneNumber(firebaseUser, credential)
                    .subscribe(this)

            // verify updatePhoneNumber() has called
            verify(firebaseUser, times(1))
                    .updatePhoneNumber(credential)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun updatePhoneNumberNotSuccessful() {
        val task = failedTask<Void>(IllegalStateException())
        val credential = mock<PhoneAuthCredential>()

        whenever(firebaseUser.updatePhoneNumber(credential))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updatePhoneNumber(firebaseUser, credential)
                    .subscribe(this)

            // verify updatePhoneNumber() has called
            verify(firebaseUser, times(1))
                    .updatePhoneNumber(credential)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun updateProfile() {
        val profile = mock<UserProfileChangeRequest>()
        val task = succeedVoidTask()

        whenever(firebaseUser.updateProfile(profile))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updateProfile(firebaseUser, profile)
                    .subscribe(this)

            // verify updateProfile() has called
            verify(firebaseUser, times(1))
                    .updateProfile(profile)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun updateProfileNotSuccessful() {
        val profile = mock<UserProfileChangeRequest>()
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseUser.updateProfile(profile))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseUser.updateProfile(firebaseUser, profile)
                    .subscribe(this)

            // verify updateProfile() has called
            verify(firebaseUser, times(1))
                    .updateProfile(profile)

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @JvmName("verifyAuthResultAddOnCompleteListenerCalled")
    private inline fun Task<AuthResult>.verifyAddOnCompleteListenerCalled() {
        verify(this, times(1))
                .addOnCompleteListener(onAuthResultCompleteListener.capture())
    }

    @JvmName("verifyGetTokenResultAddOnCompleteListenerCalled")
    private inline fun Task<GetTokenResult>.verifyAddOnCompleteListenerCalled() {
        verify(this, times(1))
                .addOnCompleteListener(onGetTokenResultCompleteListener.capture())
    }

    @JvmName("verifyVoidAddOnCompleteListenerCalled")
    private inline fun Task<Void>.verifyAddOnCompleteListenerCalled() {
        verify(this, times(1))
                .addOnCompleteListener(onVoidCompleteListener.capture())
    }
}