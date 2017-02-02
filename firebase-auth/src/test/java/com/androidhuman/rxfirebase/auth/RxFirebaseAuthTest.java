package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import com.memoizrlabs.retrooptional.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RxFirebaseAuthTest {

    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Mock
    AuthCredential mockAuthCredential;

    @Mock
    AuthResult mockAuthResult;

    @Mock
    Task<AuthResult> mockAuthResultTask;

    @Mock
    Task<Void> mockSendPasswordResetEmailTask;

    @Mock
    Task<ProviderQueryResult> mockFetchProvidersTask;

    @Mock
    FirebaseUser mockFirebaseUser;

    private ArgumentCaptor<OnCompleteListener> onComplete;

    private ArgumentCaptor<FirebaseAuth.AuthStateListener> authStateChange;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        onComplete = ArgumentCaptor.forClass(OnCompleteListener.class);
        authStateChange = ArgumentCaptor.forClass(FirebaseAuth.AuthStateListener.class);
    }

    @Test
    public void testAuthStateChanges() {
        TestObserver<FirebaseAuth> obs = TestObserver.create();

        RxFirebaseAuth.authStateChanges(mockFirebaseAuth)
                .subscribe(obs);

        callOnAuthStateChanged();

        obs.assertNotComplete();
        obs.assertValueCount(1);

        obs.dispose();

        callOnAuthStateChanged();

        // Assert no more values are emitted
        obs.assertValueCount(1);
    }

    @Test
    public void testCreateUserWithEmailAndPassword() {
        when(mockFirebaseUser.getEmail())
                .thenReturn("foo@bar.com");

        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.createUserWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .createUserWithEmailAndPassword(mockFirebaseAuth, "foo@bar.com", "password")
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(new Predicate<FirebaseUser>() {
            @Override
            public boolean test(FirebaseUser firebaseUser) throws Exception {
                return "foo@bar.com".equals(firebaseUser.getEmail());
            }
        });
    }

    @Test
    public void testCreateUserWithEmailAndPassword_NotSuccessful() {
        when(mockFirebaseUser.getEmail())
                .thenReturn("foo@bar.com");

        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.createUserWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .createUserWithEmailAndPassword(mockFirebaseAuth, "foo@bar.com", "password")
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testFetchProvidersForEmail() {
        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        mockSuccessfulFetchProvidersResult();

        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        TestObserver<Optional<List<String>>> obs = TestObserver.create();

        RxFirebaseAuth
                .fetchProvidersForEmail(mockFirebaseAuth, "foo@bar.com")
                .subscribe(obs);

        callOnComplete(mockFetchProvidersTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockFetchProvidersTask);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testFetchProvidersForEmail_NotSuccessful() {
        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        mockNotSuccessfulFetchProvidersResult(new IllegalStateException());

        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        TestObserver<Optional<List<String>>> obs = TestObserver.create();

        RxFirebaseAuth
                .fetchProvidersForEmail(mockFirebaseAuth, "foo@bar.com")
                .subscribe(obs);

        callOnComplete(mockFetchProvidersTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockFetchProvidersTask);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testGetCurrentUser_notSignedIn() {
        when(mockFirebaseAuth.getCurrentUser())
                .thenReturn(null);

        TestObserver<Optional<FirebaseUser>> obs = TestObserver.create();

        RxFirebaseAuth.getCurrentUser(mockFirebaseAuth)
                .subscribe(obs);

        verify(mockFirebaseAuth)
                .getCurrentUser();

        obs.dispose();

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(new Predicate<Optional<FirebaseUser>>() {
            @Override
            public boolean test(Optional<FirebaseUser> firebaseUserOptional) throws Exception {
                if (firebaseUserOptional.isPresent()) {
                    return false;
                }
                try {
                    firebaseUserOptional.get();
                    failBecauseExceptionWasNotThrown(NoSuchElementException.class);
                    return false;
                } catch (Exception e) {
                    return e instanceof NoSuchElementException;
                }
            }
        });
    }

    @Test
    public void testGetCurrentUser_signedIn() {
        when(mockFirebaseUser.getDisplayName())
                .thenReturn("John Doe");

        when(mockFirebaseAuth.getCurrentUser())
                .thenReturn(mockFirebaseUser);

        TestObserver<Optional<FirebaseUser>> obs = TestObserver.create();

        RxFirebaseAuth.getCurrentUser(mockFirebaseAuth)
                .subscribe(obs);

        verify(mockFirebaseAuth)
                .getCurrentUser();

        obs.dispose();

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(new Predicate<Optional<FirebaseUser>>() {
            @Override
            public boolean test(Optional<FirebaseUser> firebaseUserOptional) throws Exception {
                return firebaseUserOptional.isPresent() && "John Doe"
                        .equals(firebaseUserOptional.get().getDisplayName());

            }
        });
    }

    @Test
    public void testSendPasswordResetEmail() {
        when(mockFirebaseAuth.sendPasswordResetEmail("email"))
                .thenReturn(mockSendPasswordResetEmailTask);

        mockSuccessfulSendPasswordResetEmailResult();

        TestObserver obs = TestObserver.create();

        RxFirebaseAuth
                .sendPasswordResetEmail(mockFirebaseAuth, "email")
                .subscribe(obs);

        callOnComplete(mockSendPasswordResetEmailTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockSendPasswordResetEmailTask);

        verify(mockFirebaseAuth)
                .sendPasswordResetEmail("email");

        obs.assertNoErrors();

        obs.assertComplete();
    }

    @Test
    public void testSendPasswordResetEmail_NotSuccessful() {
        when(mockFirebaseAuth.sendPasswordResetEmail("email"))
                .thenReturn(mockSendPasswordResetEmailTask);

        mockNotSuccessfulSendPasswordResetEmailResult(new IllegalStateException());

        TestObserver obs = TestObserver.create();

        RxFirebaseAuth
                .sendPasswordResetEmail(mockFirebaseAuth, "email")
                .subscribe(obs);

        callOnComplete(mockSendPasswordResetEmailTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockSendPasswordResetEmailTask);

        verify(mockFirebaseAuth)
                .sendPasswordResetEmail("email");

        obs.assertError(IllegalStateException.class);
        obs.assertNotComplete();
    }

    @Test
    public void testSignInAnonymous() {
        when(mockFirebaseUser.isAnonymous())
                .thenReturn(true);

        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInAnonymously())
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth.signInAnonymous(mockFirebaseAuth)
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertNoErrors();

        obs.assertValue(new Predicate<FirebaseUser>() {
            @Override
            public boolean test(FirebaseUser firebaseUser) throws Exception {
                return firebaseUser.isAnonymous();
            }
        });
    }

    @Test
    public void testSignInAnonymous_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInAnonymously())
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth.signInAnonymous(mockFirebaseAuth)
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testSignInWithCredential() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithCredential(mockAuthCredential))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .signInWithCredential(mockFirebaseAuth, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testSignInWithCredential_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithCredential(mockAuthCredential))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .signInWithCredential(mockFirebaseAuth, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testSignInWithCustomToken() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .signInWithCustomToken(mockFirebaseAuth, "custom_token")
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testSignInWithCustomToken_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .signInWithCustomToken(mockFirebaseAuth, "custom_token")
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testSignInWithEmailAndPassword() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .signInWithEmailAndPassword(mockFirebaseAuth, "email", "password")
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testSignInWithEmailAndPassword_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(mockAuthResultTask);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseAuth
                .signInWithEmailAndPassword(mockFirebaseAuth, "email", "password")
                .subscribe(obs);

        callOnComplete(mockAuthResultTask);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testSignOut() {
        TestObserver obs = TestObserver.create();

        RxFirebaseAuth.signOut(mockFirebaseAuth)
                .subscribe(obs);

        verify(mockFirebaseAuth)
                .signOut();

        obs.dispose();

        obs.assertNoErrors();
        obs.assertComplete();
    }

    private void mockSuccessfulAuthResult() {
        when(mockAuthResult.getUser())
                .thenReturn(mockFirebaseUser);

        when(mockAuthResultTask.isSuccessful())
                .thenReturn(true);

        when(mockAuthResultTask.getResult())
                .thenReturn(mockAuthResult);

        //noinspection unchecked
        when(mockAuthResultTask.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockAuthResultTask);
    }

    private void mockNotSuccessfulResult(Exception exception) {
        when(mockAuthResultTask.isSuccessful())
                .thenReturn(false);

        when(mockAuthResultTask.getException())
                .thenReturn(exception);

        //noinspection unchecked
        when(mockAuthResultTask.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockAuthResultTask);
    }

    private void mockSuccessfulSendPasswordResetEmailResult() {
        when(mockSendPasswordResetEmailTask.isSuccessful())
                .thenReturn(true);

        //noinspection unchecked
        when(mockSendPasswordResetEmailTask.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockSendPasswordResetEmailTask);
    }

    private void mockNotSuccessfulSendPasswordResetEmailResult(Exception exception) {
        when(mockSendPasswordResetEmailTask.isSuccessful())
                .thenReturn(false);

        when(mockSendPasswordResetEmailTask.getException())
                .thenReturn(exception);

        //noinspection unchecked
        when(mockSendPasswordResetEmailTask.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockSendPasswordResetEmailTask);
    }

    private void mockSuccessfulFetchProvidersResult() {
        when(mockFetchProvidersTask.isSuccessful())
                .thenReturn(true);

        //noinspection unchecked
        when(mockFetchProvidersTask.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockFetchProvidersTask);
    }

    private void mockNotSuccessfulFetchProvidersResult(Exception exception) {
        when(mockFetchProvidersTask.isSuccessful())
                .thenReturn(false);

        when(mockFetchProvidersTask.getException())
                .thenReturn(exception);

        //noinspection unchecked
        when(mockFetchProvidersTask.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockFetchProvidersTask);
    }

    @SuppressWarnings("unchecked")
    private void callOnComplete(Task<?> task) {
        verify(task)
                .addOnCompleteListener(onComplete.capture());
        onComplete.getValue().onComplete(task);
    }

    private void callOnAuthStateChanged() {
        verify(mockFirebaseAuth)
                .addAuthStateListener(authStateChange.capture());
        authStateChange.getValue().onAuthStateChanged(mockFirebaseAuth);
    }
}
