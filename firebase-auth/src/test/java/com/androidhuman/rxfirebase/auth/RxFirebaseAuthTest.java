package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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
        TestSubscriber<FirebaseAuth> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.authStateChanges(mockFirebaseAuth)
                .subscribe(sub);

        callOnAuthStateChanged();

        sub.assertNotCompleted();
        sub.assertValueCount(1);

        s.unsubscribe();

        callOnAuthStateChanged();

        // Assert no more values are emitted
        sub.assertValueCount(1);
    }

    @Test
    public void testCreateUserWithEmailAndPassword() {
        when(mockFirebaseUser.getEmail())
                .thenReturn("foo@bar.com");

        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.createUserWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .createUserWithEmailAndPassword(mockFirebaseAuth, "foo@bar.com", "password")
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        FirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.getEmail())
                .isEqualTo("foo@bar.com");
    }

    @Test
    public void testCreateUserWithEmailAndPassword_NotSuccessful() {
        when(mockFirebaseUser.getEmail())
                .thenReturn("foo@bar.com");

        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.createUserWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .createUserWithEmailAndPassword(mockFirebaseAuth, "foo@bar.com", "password")
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testFetchProvidersForEmail() {
        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        mockSuccessfulFetchProvidersResult();

        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        TestSubscriber<List<String>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .fetchProvidersForEmail(mockFirebaseAuth, "foo@bar.com")
                .subscribe(sub);

        callOnComplete(mockFetchProvidersTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockFetchProvidersTask);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);
    }

    @Test
    public void testFetchProvidersForEmail_NotSuccessful() {
        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        mockNotSuccessfulFetchProvidersResult(new IllegalStateException());

        when(mockFirebaseAuth.fetchProvidersForEmail("foo@bar.com"))
                .thenReturn(mockFetchProvidersTask);

        TestSubscriber<List<String>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .fetchProvidersForEmail(mockFirebaseAuth, "foo@bar.com")
                .subscribe(sub);

        callOnComplete(mockFetchProvidersTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockFetchProvidersTask);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testGetCurrentUser_notSignedIn() {
        when(mockFirebaseAuth.getCurrentUser())
                .thenReturn(null);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.getCurrentUser(mockFirebaseAuth)
                .subscribe(sub);

        verify(mockFirebaseAuth)
                .getCurrentUser();

        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertNoValues();
    }

    @Test
    public void testGetCurrentUser_signedIn() {
        when(mockFirebaseUser.getDisplayName())
                .thenReturn("John Doe");

        when(mockFirebaseAuth.getCurrentUser())
                .thenReturn(mockFirebaseUser);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.getCurrentUser(mockFirebaseAuth)
                .subscribe(sub);

        verify(mockFirebaseAuth)
                .getCurrentUser();

        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        FirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.getDisplayName())
                .isEqualTo("John Doe");
    }

    @Test
    public void testSendPasswordResetEmail() {
        when(mockFirebaseAuth.sendPasswordResetEmail("email"))
                .thenReturn(mockSendPasswordResetEmailTask);

        mockSuccessfulSendPasswordResetEmailResult();

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseAuth
                .sendPasswordResetEmail(mockFirebaseAuth, "email")
                .subscribe(sub);

        callOnComplete(mockSendPasswordResetEmailTask);

        verify(mockFirebaseAuth)
                .sendPasswordResetEmail("email");

        sub.assertNoErrors();
        sub.assertCompleted();
    }

    @Test
    public void testSendPasswordResetEmail_NotSuccessful() {
        when(mockFirebaseAuth.sendPasswordResetEmail("email"))
                .thenReturn(mockSendPasswordResetEmailTask);

        mockNotSuccessfulSendPasswordResetEmailResult(new IllegalStateException());

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseAuth
                .sendPasswordResetEmail(mockFirebaseAuth, "email")
                .subscribe(sub);

        callOnComplete(mockSendPasswordResetEmailTask);

        verify(mockFirebaseAuth)
                .sendPasswordResetEmail("email");

        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testSignInAnonymous() {
        when(mockFirebaseUser.isAnonymous())
                .thenReturn(true);

        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInAnonymously())
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.signInAnonymous(mockFirebaseAuth)
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        FirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.isAnonymous())
                .isTrue();
    }

    @Test
    public void testSignInAnonymous_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInAnonymously())
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.signInAnonymous(mockFirebaseAuth)
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignInWithCredential() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithCredential(mockAuthCredential))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCredential(mockFirebaseAuth, mockAuthCredential)
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);
    }

    @Test
    public void testSignInWithCredential_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithCredential(mockAuthCredential))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCredential(mockFirebaseAuth, mockAuthCredential)
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignInWithCustomToken() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCustomToken(mockFirebaseAuth, "custom_token")
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);
    }

    @Test
    public void testSignInWithCustomToken_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCustomToken(mockFirebaseAuth, "custom_token")
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignInWithEmailAndPassword() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithEmailAndPassword(mockFirebaseAuth, "email", "password")
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);
    }

    @Test
    public void testSignInWithEmailAndPassword_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<FirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithEmailAndPassword(mockFirebaseAuth, "email", "password")
                .subscribe(sub);

        callOnComplete(mockAuthResultTask);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthResultTask);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignOut() {
        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseAuth.signOut(mockFirebaseAuth)
                .subscribe(sub);

        verify(mockFirebaseAuth)
                .signOut();

        sub.assertNoErrors();
        sub.assertCompleted();
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

        ProviderQueryResult result = mock(ProviderQueryResult.class);
        when(result.getProviders())
                .thenReturn(new ArrayList<String>());

        when(mockFetchProvidersTask.getResult())
                .thenReturn(result);

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
