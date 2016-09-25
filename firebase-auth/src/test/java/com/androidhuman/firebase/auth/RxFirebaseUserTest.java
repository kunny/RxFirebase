package com.androidhuman.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.androidhuman.firebase.auth.model.TaskResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RxFirebaseUserTest {

    @Mock
    FirebaseUser mockFirebaseUser;

    @Mock
    GetTokenResult mockGetTokenResult;

    @Mock
    AuthCredential mockAuthCredential;

    @Mock
    AuthResult mockAuthResult;

    @Mock
    UserProfileChangeRequest mockProfileChangeRequest;

    @Mock
    Task<Void> mockVoidTaskResult;

    @Mock
    Task<GetTokenResult> mockGetTokenTaskResult;

    @Mock
    Task<AuthResult> mockAuthTaskResult;

    private ArgumentCaptor<OnCompleteListener> onComplete;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        onComplete = ArgumentCaptor.forClass(OnCompleteListener.class);
    }

    @Test
    public void testDelete() {
        mockVoidResult(true);
        when(mockFirebaseUser.delete())
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.delete(mockFirebaseUser)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();
    }

    @Test
    public void testDelete_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.delete())
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.delete(mockFirebaseUser)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();
    }

    @Test
    public void testGetToken() {
        mockSuccessfulTokenResult("token");
        when(mockFirebaseUser.getToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestSubscriber<String> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.getToken(mockFirebaseUser, true)
                .subscribe(sub);

        callOnComplete(mockGetTokenTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        String result = sub.getOnNextEvents().get(0);

        assertThat(result)
                .isEqualTo("token");
    }

    @Test
    public void testGetToken_notSuccessful() {
        mockNotSuccessfulTokenResult(new IllegalStateException());
        when(mockFirebaseUser.getToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestSubscriber<String> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.getToken(mockFirebaseUser, true)
                .subscribe(sub);

        callOnComplete(mockGetTokenTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testLinkWithCredential() {
        mockSuccessfulAuthResult();
        when(mockFirebaseUser.linkWithCredential(mockAuthCredential))
                .thenReturn(mockAuthTaskResult);

        TestSubscriber<AuthResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.linkWithCredential(mockFirebaseUser, mockAuthCredential)
                .subscribe(sub);

        callOnComplete(mockAuthTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);
    }

    @Test
    public void testLinkWithCredential_notSuccessful() {
        mockNotSuccessfulAuthResult(new IllegalStateException());
        when(mockFirebaseUser.linkWithCredential(mockAuthCredential))
                .thenReturn(mockAuthTaskResult);

        TestSubscriber<AuthResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.linkWithCredential(mockFirebaseUser, mockAuthCredential)
                .subscribe(sub);

        callOnComplete(mockAuthTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testReauthenticate() {
        mockVoidResult(true);
        when(mockFirebaseUser.reauthenticate(mockAuthCredential))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.reauthenticate(mockFirebaseUser, mockAuthCredential)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();
    }

    @Test
    public void testReauthenticate_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.reauthenticate(mockAuthCredential))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.reauthenticate(mockFirebaseUser, mockAuthCredential)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();
    }

    @Test
    public void testReload() {
        mockVoidResult(true);
        when(mockFirebaseUser.reload())
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.reload(mockFirebaseUser)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();
    }

    @Test
    public void testReload_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.reload())
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.reload(mockFirebaseUser)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();
    }

    @Test
    public void testSendEmailVerification() {
        mockVoidResult(true);
        when(mockFirebaseUser.sendEmailVerification())
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.sendEmailVerification(mockFirebaseUser)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();
    }

    @Test
    public void testSendEmailVerification_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.sendEmailVerification())
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.sendEmailVerification(mockFirebaseUser)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();
    }

    @Test
    public void testUnlink() {
        mockSuccessfulAuthResult();
        when(mockFirebaseUser.unlink("provider"))
                .thenReturn(mockAuthTaskResult);

        TestSubscriber<AuthResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.unlink(mockFirebaseUser, "provider")
                .subscribe(sub);

        callOnComplete(mockAuthTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);
    }

    @Test
    public void testUnlink_notSuccessful() {
        mockNotSuccessfulAuthResult(new IllegalStateException());
        when(mockFirebaseUser.unlink("provider"))
                .thenReturn(mockAuthTaskResult);

        TestSubscriber<AuthResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.unlink(mockFirebaseUser, "provider")
                .subscribe(sub);

        callOnComplete(mockAuthTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        sub.assertError(IllegalStateException.class);
        sub.assertNoValues();
    }

    @Test
    public void testUpdateEmail() {
        mockVoidResult(true);
        when(mockFirebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.updateEmail(mockFirebaseUser, "foo@bar.com")
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();
    }

    @Test
    public void testUpdateEmail_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.updateEmail(mockFirebaseUser, "foo@bar.com")
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();
    }

    @Test
    public void testUpdatePassword() {
        mockVoidResult(true);
        when(mockFirebaseUser.updatePassword("password"))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.updatePassword(mockFirebaseUser, "password")
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();
    }

    @Test
    public void testUpdatePassword_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.updatePassword("password"))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.updatePassword(mockFirebaseUser, "password")
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();
    }

    @Test
    public void testUpdateProfile() {
        mockVoidResult(true);
        when(mockFirebaseUser.updateProfile(mockProfileChangeRequest))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.updateProfile(mockFirebaseUser, mockProfileChangeRequest)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();
    }

    @Test
    public void testUpdateProfile_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.updateProfile(mockProfileChangeRequest))
                .thenReturn(mockVoidTaskResult);

        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseUser.updateProfile(mockFirebaseUser, mockProfileChangeRequest)
                .subscribe(sub);

        callOnComplete(mockVoidTaskResult);
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();
    }

    private void mockVoidResult(boolean success) {
        when(mockVoidTaskResult.isSuccessful())
                .thenReturn(success);

        //noinspection unchecked
        when(mockVoidTaskResult.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockVoidTaskResult);
    }

    private void mockSuccessfulTokenResult(String token) {
        when(mockGetTokenResult.getToken())
                .thenReturn(token);

        when(mockGetTokenTaskResult.isSuccessful())
                .thenReturn(true);

        when(mockGetTokenTaskResult.getResult())
                .thenReturn(mockGetTokenResult);

        //noinspection unchecked
        when(mockGetTokenTaskResult.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockGetTokenTaskResult);
    }

    private void mockNotSuccessfulTokenResult(Exception exception) {
        when(mockGetTokenTaskResult.isSuccessful())
                .thenReturn(false);

        when(mockGetTokenTaskResult.getException())
                .thenReturn(exception);

        when(mockGetTokenTaskResult.getResult())
                .thenReturn(mockGetTokenResult);

        //noinspection unchecked
        when(mockGetTokenTaskResult.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockGetTokenTaskResult);
    }

    private void mockSuccessfulAuthResult() {
        when(mockAuthTaskResult.isSuccessful())
                .thenReturn(true);

        when(mockAuthTaskResult.getResult())
                .thenReturn(mockAuthResult);

        //noinspection unchecked
        when(mockAuthTaskResult.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockAuthTaskResult);
    }

    private void mockNotSuccessfulAuthResult(Exception exception) {
        when(mockAuthTaskResult.isSuccessful())
                .thenReturn(false);

        when(mockAuthTaskResult.getException())
                .thenReturn(exception);

        when(mockAuthTaskResult.getResult())
                .thenReturn(mockAuthResult);

        //noinspection unchecked
        when(mockAuthTaskResult.addOnCompleteListener(onComplete.capture()))
                .thenReturn(mockAuthTaskResult);
    }

    @SuppressWarnings("unchecked")
    private void callOnComplete(Task<?> task) {
        verify(task)
                .addOnCompleteListener(onComplete.capture());
        onComplete.getValue().onComplete(task);
    }
}
