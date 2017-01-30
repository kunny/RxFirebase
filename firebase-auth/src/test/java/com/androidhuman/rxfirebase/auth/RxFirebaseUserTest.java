package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.support.annotation.NonNull;

import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

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

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.delete(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskSuccess());
    }

    @Test
    public void testDelete_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.delete())
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.delete(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return !taskResult.isSuccess();
            }
        });
    }

    @Test
    public void testGetToken() {
        mockSuccessfulTokenResult("token");
        when(mockFirebaseUser.getToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestObserver<String> obs = TestObserver.create();

        RxFirebaseUser.getToken(mockFirebaseUser, true)
                .subscribe(obs);

        callOnComplete(mockGetTokenTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValue("token");
    }

    @Test
    public void testGetToken_notSuccessful() {
        mockNotSuccessfulTokenResult(new IllegalStateException());
        when(mockFirebaseUser.getToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestObserver<String> obs = TestObserver.create();

        RxFirebaseUser.getToken(mockFirebaseUser, true)
                .subscribe(obs);

        callOnComplete(mockGetTokenTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testLinkWithCredential() {
        mockSuccessfulAuthResult();
        when(mockFirebaseUser.linkWithCredential(mockAuthCredential))
                .thenReturn(mockAuthTaskResult);

        TestObserver<AuthResult> obs = TestObserver.create();

        RxFirebaseUser.linkWithCredential(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testLinkWithCredential_notSuccessful() {
        mockNotSuccessfulAuthResult(new IllegalStateException());
        when(mockFirebaseUser.linkWithCredential(mockAuthCredential))
                .thenReturn(mockAuthTaskResult);

        TestObserver<AuthResult> obs = TestObserver.create();

        RxFirebaseUser.linkWithCredential(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testReauthenticate() {
        mockVoidResult(true);
        when(mockFirebaseUser.reauthenticate(mockAuthCredential))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.reauthenticate(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskSuccess());
    }

    @Test
    public void testReauthenticate_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.reauthenticate(mockAuthCredential))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.reauthenticate(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValue(new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return !taskResult.isSuccess();
            }
        });
    }

    @Test
    public void testReload() {
        mockVoidResult(true);
        when(mockFirebaseUser.reload())
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.reload(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValue(assertTaskSuccess());
    }

    @Test
    public void testReload_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.reload())
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.reload(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return !taskResult.isSuccess();
            }
        });
    }

    @Test
    public void testSendEmailVerification() {
        mockVoidResult(true);
        when(mockFirebaseUser.sendEmailVerification())
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.sendEmailVerification(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskSuccess());
    }

    @Test
    public void testSendEmailVerification_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.sendEmailVerification())
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.sendEmailVerification(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskError());
    }

    @Test
    public void testUnlink() {
        mockSuccessfulAuthResult();
        when(mockFirebaseUser.unlink("provider"))
                .thenReturn(mockAuthTaskResult);

        TestObserver<AuthResult> obs = TestObserver.create();

        RxFirebaseUser.unlink(mockFirebaseUser, "provider")
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testUnlink_notSuccessful() {
        mockNotSuccessfulAuthResult(new IllegalStateException());
        when(mockFirebaseUser.unlink("provider"))
                .thenReturn(mockAuthTaskResult);

        TestObserver<AuthResult> obs = TestObserver.create();

        RxFirebaseUser.unlink(mockFirebaseUser, "provider")
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        obs.assertError(IllegalStateException.class);
        obs.assertNoValues();
    }

    @Test
    public void testUpdateEmail() {
        mockVoidResult(true);
        when(mockFirebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.updateEmail(mockFirebaseUser, "foo@bar.com")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskSuccess());
    }

    @Test
    public void testUpdateEmail_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.updateEmail(mockFirebaseUser, "foo@bar.com")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskError());
    }

    @Test
    public void testUpdatePassword() {
        mockVoidResult(true);
        when(mockFirebaseUser.updatePassword("password"))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.updatePassword(mockFirebaseUser, "password")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskSuccess());
    }

    @Test
    public void testUpdatePassword_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.updatePassword("password"))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.updatePassword(mockFirebaseUser, "password")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskError());
    }

    @Test
    public void testUpdateProfile() {
        mockVoidResult(true);
        when(mockFirebaseUser.updateProfile(mockProfileChangeRequest))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.updateProfile(mockFirebaseUser, mockProfileChangeRequest)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskSuccess());
    }

    @Test
    public void testUpdateProfile_notSuccessful() {
        mockVoidResult(false);
        when(mockFirebaseUser.updateProfile(mockProfileChangeRequest))
                .thenReturn(mockVoidTaskResult);

        TestObserver<TaskResult> obs = TestObserver.create();

        RxFirebaseUser.updateProfile(mockFirebaseUser, mockProfileChangeRequest)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertNoErrors();
        obs.assertComplete();

        obs.assertValue(assertTaskError());
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

    private Predicate<TaskResult> assertTaskSuccess() {
        return new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return taskResult.isSuccess();
            }
        };
    }

    private Predicate<TaskResult> assertTaskError() {
        return new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return !taskResult.isSuccess();
            }
        };
    }
}
