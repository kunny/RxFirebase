package com.androidhuman.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.firebase.auth.model.OptionalFirebaseUser;
import com.androidhuman.firebase.auth.model.TaskResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
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
    FirebaseUser mockFirebaseUser;

    private ArgumentCaptor<OnCompleteListener> authOnComplete;

    private ArgumentCaptor<FirebaseAuth.AuthStateListener> authStateChange;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        authOnComplete = ArgumentCaptor.forClass(OnCompleteListener.class);
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

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .createUserWithEmailAndPassword(mockFirebaseAuth, "foo@bar.com", "password")
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        OptionalFirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.get().getEmail())
                .isEqualTo("foo@bar.com");
    }

    @Test
    public void testCreateUserWithEmailAndPassword_NotSuccessful() {
        when(mockFirebaseUser.getEmail())
                .thenReturn("foo@bar.com");

        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.createUserWithEmailAndPassword("foo@bar.com", "password"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .createUserWithEmailAndPassword(mockFirebaseAuth, "foo@bar.com", "password")
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testGetCurrentUser_notSignedIn() {
        when(mockFirebaseAuth.getCurrentUser())
                .thenReturn(null);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.getCurrentUser(mockFirebaseAuth)
                .subscribe(sub);

        verify(mockFirebaseAuth)
                .getCurrentUser();

        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        OptionalFirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.isPresent())
                .isFalse();

        try {
            user.get();
            failBecauseExceptionWasNotThrown(NoSuchElementException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    public void testGetCurrentUser_signedIn() {
        when(mockFirebaseUser.getDisplayName())
                .thenReturn("John Doe");

        when(mockFirebaseAuth.getCurrentUser())
                .thenReturn(mockFirebaseUser);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.getCurrentUser(mockFirebaseAuth)
                .subscribe(sub);

        verify(mockFirebaseAuth)
                .getCurrentUser();

        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        OptionalFirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.isPresent())
                .isTrue();

        assertThat(user.get().getDisplayName())
                .isEqualTo("John Doe");
    }

    @Test
    public void testSignInAnonymous() {
        when(mockFirebaseUser.isAnonymous())
                .thenReturn(true);

        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInAnonymously())
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.signInAnonymous(mockFirebaseAuth)
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        OptionalFirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.isPresent())
                .isTrue();
        assertThat(user.get().isAnonymous())
                .isTrue();
    }

    @Test
    public void testSignInAnonymous_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInAnonymously())
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.signInAnonymous(mockFirebaseAuth)
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignInWithCredential() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithCredential(mockAuthCredential))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCredential(mockFirebaseAuth, mockAuthCredential)
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        OptionalFirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.isPresent())
                .isTrue();
    }

    @Test
    public void testSignInWithCredential_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithCredential(mockAuthCredential))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCredential(mockFirebaseAuth, mockAuthCredential)
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignInWithCustomToken() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCustomToken(mockFirebaseAuth, "custom_token")
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        OptionalFirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.isPresent())
                .isTrue();
    }

    @Test
    public void testSignInWithCustomToken_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithCustomToken("custom_token"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithCustomToken(mockFirebaseAuth, "custom_token")
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignInWithEmailAndPassword() {
        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithEmailAndPassword(mockFirebaseAuth, "email", "password")
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        OptionalFirebaseUser user = sub.getOnNextEvents().get(0);

        assertThat(user.isPresent())
                .isTrue();
    }

    @Test
    public void testSignInWithEmailAndPassword_NotSuccessful() {
        mockNotSuccessfulResult(new IllegalStateException());

        when(mockFirebaseAuth.signInWithEmailAndPassword("email", "password"))
                .thenReturn(mockAuthResultTask);

        TestSubscriber<OptionalFirebaseUser> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth
                .signInWithEmailAndPassword(mockFirebaseAuth, "email", "password")
                .subscribe(sub);

        callOnComplete();
        s.unsubscribe();

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testSignOut() {
        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseAuth.signOut(mockFirebaseAuth)
                .subscribe(sub);

        verify(mockFirebaseAuth)
                .signOut();

        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        assertThat(sub.getOnNextEvents().get(0).isSuccess())
                .isTrue();
    }

    private void mockSuccessfulAuthResult() {
        when(mockAuthResult.getUser())
                .thenReturn(mockFirebaseUser);

        when(mockAuthResultTask.isSuccessful())
                .thenReturn(true);

        when(mockAuthResultTask.getResult())
                .thenReturn(mockAuthResult);

        //noinspection unchecked
        when(mockAuthResultTask.addOnCompleteListener(authOnComplete.capture()))
                .thenReturn(mockAuthResultTask);
    }

    private void mockNotSuccessfulResult(Exception exception) {
        when(mockAuthResultTask.isSuccessful())
                .thenReturn(false);

        when(mockAuthResultTask.getException())
                .thenReturn(exception);

        //noinspection unchecked
        when(mockAuthResultTask.addOnCompleteListener(authOnComplete.capture()))
                .thenReturn(mockAuthResultTask);
    }

    @SuppressWarnings("unchecked")
    private void callOnComplete() {
        verify(mockAuthResultTask)
                .addOnCompleteListener(authOnComplete.capture());
        authOnComplete.getValue().onComplete(mockAuthResultTask);
    }

    private void callOnAuthStateChanged() {
        verify(mockFirebaseAuth)
                .addAuthStateListener(authStateChange.capture());
        authStateChange.getValue().onAuthStateChanged(mockFirebaseAuth);
    }
}
