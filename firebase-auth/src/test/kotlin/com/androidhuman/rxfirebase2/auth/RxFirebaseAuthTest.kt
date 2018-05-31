package com.androidhuman.rxfirebase2.auth

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ProviderQueryResult
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@Suppress("NOTHING_TO_INLINE")
class RxFirebaseAuthTest {

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    private val authStateListener
            = argumentCaptor<FirebaseAuth.AuthStateListener>()

    private val onAuthResultCompleteListener
            = argumentCaptor<OnCompleteListener<AuthResult>>()

    private val onProviderQueryResultCompleteListener
            = argumentCaptor<OnCompleteListener<ProviderQueryResult>>()

    private val onVoidCompleteListener
            = argumentCaptor<OnCompleteListener<Void>>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun authStateChanged() {
        with(TestObserver.create<FirebaseAuth>()) {
            RxFirebaseAuth.authStateChanges(firebaseAuth)
                    .subscribe(this)

            // verify addAuthStateListener has called
            firebaseAuth.verifyAddAuthStateListenerCalled()

            // simulate the callback
            authStateListener.lastValue.onAuthStateChanged(firebaseAuth)

            assertNotComplete()
            assertValue(firebaseAuth)
            assertValueCount(1)

            dispose()

            // simulate the callback
            authStateListener.lastValue.onAuthStateChanged(firebaseAuth)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun createUserWithEmailAndPassword() {
        val task = succeedAuthResultTask("foo@bar.com")

        whenever(firebaseAuth.createUserWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.createUserWithEmailAndPassword(
                    firebaseAuth, "foo@bar.com", "password")
                    .subscribe(this)

            // verify createUserWithEmailAndPassword() has called
            verify(firebaseAuth, times(1))
                    .createUserWithEmailAndPassword("foo@bar.com", "password")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertValue { "foo@bar.com" == it.email }
            assertComplete()

            dispose()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun createUserWithEmailAndPasswordNotSuccessful() {
        val task = failedTask<AuthResult>(IllegalStateException())

        whenever(firebaseAuth.createUserWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.createUserWithEmailAndPassword(
                    firebaseAuth, "foo@bar.com", "password")
                    .subscribe(this)

            // verify createUserWithEmailAndPassword() has called
            verify(firebaseAuth, times(1))
                    .createUserWithEmailAndPassword("foo@bar.com", "password")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            // assert no values are emitted
            assertNoValues()
        }
    }

    @Test
    fun fetchProvidersForEmail() {
        val task = succeedProvidersResultTask(listOf("pro", "vid", "ers"))

        whenever(firebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(task)

        with(TestObserver.create<List<String>>()) {
            RxFirebaseAuth.fetchSignInMethodsForEmail(
                    firebaseAuth, "foo@bar.com")
                    .subscribe(this)

            // verify fetchSignInMethodsForEmail() has called
            verify(firebaseAuth, times(1))
                    .fetchProvidersForEmail("foo@bar.com")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onProviderQueryResultCompleteListener.lastValue.onComplete(task)

            assertThat(values().first())
                    .contains("pro", "vid", "ers")
            assertComplete()

            dispose()

            // simulate the callback
            onProviderQueryResultCompleteListener.lastValue.onComplete(task)

            // assert no values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun testProvidersForEmailNullProviders() {
        val task = succeedProvidersResultTask(null)

        whenever(firebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(task)

        with(TestObserver.create<List<String>>()) {
            RxFirebaseAuth.fetchSignInMethodsForEmail(
                    firebaseAuth, "foo@bar.com")
                    .subscribe(this)

            // verify fetchSignInMethodsForEmail() has called
            verify(firebaseAuth, times(1))
                    .fetchProvidersForEmail("foo@bar.com")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onProviderQueryResultCompleteListener.lastValue.onComplete(task)

            assertNoValues()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun fetchProvidersForEmailNotSuccessful() {
        val task = failedTask<ProviderQueryResult>(IllegalStateException())

        whenever(firebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(task)

        with(TestObserver.create<List<String>>()) {
            RxFirebaseAuth.fetchSignInMethodsForEmail(
                    firebaseAuth, "foo@bar.com")
                    .subscribe(this)

            // verify fetchSignInMethodsForEmail() has called
            verify(firebaseAuth, times(1))
                    .fetchProvidersForEmail("foo@bar.com")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onProviderQueryResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()

            // simulate the callback
            onProviderQueryResultCompleteListener.lastValue.onComplete(task)

            // assert no values are emitted
            assertNoValues()
        }
    }

    @Test
    fun getCurrentUserNotSignedIn() {
        whenever(firebaseAuth.currentUser)
                .thenReturn(null)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.getCurrentUser(firebaseAuth)
                    .subscribe(this)

            // verify getCurrentUser() has called
            verify(firebaseAuth)
                    .currentUser

            assertNoValues()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun getCurrentUserSignedIn() {
        val user = mock<FirebaseUser>().apply {
            whenever(displayName)
                    .thenReturn("John Doe")

            whenever(email)
                    .thenReturn("foo@bar.com")
        }

        whenever(firebaseAuth.currentUser)
                .thenReturn(user)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.getCurrentUser(firebaseAuth)
                    .subscribe(this)

            // verify getCurrentUser() has called
            verify(firebaseAuth)
                    .currentUser

            assertValue { "John Doe" == it.displayName && "foo@bar.com" == it.email }

            dispose()
        }
    }

    @Test
    fun sendPasswordResetEmail() {
        val task = succeedVoidTask()

        whenever(firebaseAuth.sendPasswordResetEmail("foo@bar.com"))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseAuth.sendPasswordResetEmail(firebaseAuth, "foo@bar.com")
                    .subscribe(this)

            // verify sendPasswordResetEmail() has called
            verify(firebaseAuth, times(1))
                    .sendPasswordResetEmail("foo@bar.com")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertComplete()

            dispose()
        }
    }

    @Test
    fun sendPasswordResetEmailNotSuccessful() {
        val task = failedTask<Void>(IllegalStateException())

        whenever(firebaseAuth.sendPasswordResetEmail("foo@bar.com"))
                .thenReturn(task)

        with(TestObserver.create<Any>()) {
            RxFirebaseAuth.sendPasswordResetEmail(firebaseAuth, "foo@bar.com")
                    .subscribe(this)

            // verify sendPasswordResetEmail() has called
            verify(firebaseAuth, times(1))
                    .sendPasswordResetEmail("foo@bar.com")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onVoidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun signInAnonymously() {
        val task = succeedAuthResultTask()

        whenever(firebaseAuth.signInAnonymously())
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInAnonymously(firebaseAuth)
                    .subscribe(this)

            // verify signInAnonymously() has called
            verify(firebaseAuth, times(1))
                    .signInAnonymously()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertValue { it.isAnonymous }

            dispose()
        }
    }

    @Test
    fun signInAnonymousNotSuccessful() {
        val task = failedTask<AuthResult>(IllegalStateException())

        whenever(firebaseAuth.signInAnonymously())
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInAnonymously(firebaseAuth)
                    .subscribe(this)

            // verify signInAnonymously() has called
            verify(firebaseAuth, times(1))
                    .signInAnonymously()

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun signInWithCredential() {
        val credential = mock<AuthCredential>()
        val task = succeedAuthResultTask("foo@bar.com")

        whenever(firebaseAuth.signInWithCredential(credential))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInWithCredential(firebaseAuth, credential)
                    .subscribe(this)

            // verify signInWithCredential() has called
            verify(firebaseAuth, times(1))
                    .signInWithCredential(credential)

            // verify addOnCompleteListener has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertValue { "foo@bar.com" == it.email }

            dispose()
        }
    }

    @Test
    fun signInWithCredentialNotSuccessful() {
        val credential = mock<AuthCredential>()
        val task = failedTask<AuthResult>(IllegalStateException())

        whenever(firebaseAuth.signInWithCredential(credential))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInWithCredential(firebaseAuth, credential)
                    .subscribe(this)

            // verify signInWithCredential() has called
            verify(firebaseAuth, times(1))
                    .signInWithCredential(credential)

            // verify addOnCompleteListener has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun signInWithCustomToken() {
        val task = succeedAuthResultTask("foo@bar.com")

        whenever(firebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInWithCustomToken(firebaseAuth, "custom_token")
                    .subscribe(this)

            // verify signInWithCustomToken() has called
            verify(firebaseAuth, times(1))
                    .signInWithCustomToken("custom_token")

            // verify addOnCompleteListener has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertNoErrors()

            dispose()
        }
    }

    @Test
    fun signInWithCustomTokenNotSuccessful() {
        val task = failedTask<AuthResult>(IllegalStateException())

        whenever(firebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInWithCustomToken(firebaseAuth, "custom_token")
                    .subscribe(this)

            // verify signInWithCustomToken() has called
            verify(firebaseAuth, times(1))
                    .signInWithCustomToken("custom_token")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun signInWithEmailAndPassword() {
        val task = succeedAuthResultTask("foo@bar.com")

        whenever(firebaseAuth.signInWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, "foo@bar.com", "password")
                    .subscribe(this)

            // verify signInWithEmailAndPassword() has called
            verify(firebaseAuth, times(1))
                    .signInWithEmailAndPassword("foo@bar.com", "password")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertValue { "foo@bar.com" == it.email }

            dispose()
        }
    }

    @Test
    fun signInWithEmailAndPasswordNotSuccessful() {
        val task = failedTask<AuthResult>(IllegalStateException())

        whenever(firebaseAuth.signInWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(task)

        with(TestObserver.create<FirebaseUser>()) {
            RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, "foo@bar.com", "password")
                    .subscribe(this)

            // verify signInWithEmailAndPassword() has called
            verify(firebaseAuth, times(1))
                    .signInWithEmailAndPassword("foo@bar.com", "password")

            // verify addOnCompleteListener() has called
            task.verifyAddOnCompleteListenerCalled()

            // simulate the callback
            onAuthResultCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun signOut() {
        with(TestObserver<Any>()) {
            RxFirebaseAuth.signOut(firebaseAuth)
                    .subscribe(this)

            // verify signOut() has called
            verify(firebaseAuth)
                    .signOut()

            assertComplete()

            dispose()
        }
    }

    private inline fun FirebaseAuth.verifyAddAuthStateListenerCalled() {
        verify(this, times(1))
                .addAuthStateListener(authStateListener.capture())
    }

    @JvmName("verifyAuthResultAddOnCompleteListenerCalled")
    private inline fun Task<AuthResult>.verifyAddOnCompleteListenerCalled() {
        verify(this, times(1))
                .addOnCompleteListener(onAuthResultCompleteListener.capture())
    }

    @JvmName("verifyProviderQueryAddOnCompleteListenerCalled")
    private inline fun Task<ProviderQueryResult>.verifyAddOnCompleteListenerCalled() {
        verify(this, times(1))
                .addOnCompleteListener(onProviderQueryResultCompleteListener.capture())
    }

    @JvmName("verifyVoidAddOnCompleteListenerCalled")
    private inline fun Task<Void>.verifyAddOnCompleteListenerCalled() {
        verify(this, times(1))
                .addOnCompleteListener(onVoidCompleteListener.capture())
    }
}
