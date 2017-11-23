package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        mockSuccessfulVoidResult();
        when(mockFirebaseUser.delete())
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.delete(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertComplete();
    }

    @Test
    public void testDelete_notSuccessful() {
        mockNotSuccessfulVoidResult(new IllegalStateException());
        when(mockFirebaseUser.delete())
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.delete(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testGetToken() {
        mockSuccessfulTokenResult("token");
        //noinspection deprecation
        when(mockFirebaseUser.getToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestObserver<String> obs = TestObserver.create();

        //noinspection deprecation
        RxFirebaseUser.getToken(mockFirebaseUser, true)
                .subscribe(obs);

        callOnComplete(mockGetTokenTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        obs.assertComplete();
        obs.assertValue("token");
    }

    @Test
    public void testGetToken_notSuccessful() {
        mockNotSuccessfulTokenResult(new IllegalStateException());
        //noinspection deprecation
        when(mockFirebaseUser.getToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestObserver<String> obs = TestObserver.create();

        //noinspection deprecation
        RxFirebaseUser.getToken(mockFirebaseUser, true)
                .subscribe(obs);

        callOnComplete(mockGetTokenTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testGetIdToken() {
        mockSuccessfulTokenResult("token");
        when(mockFirebaseUser.getIdToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestObserver<String> obs = TestObserver.create();

        RxFirebaseUser.getIdToken(mockFirebaseUser, true)
                .subscribe(obs);

        callOnComplete(mockGetTokenTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        obs.assertComplete();
        obs.assertValue("token");
    }

    @Test
    public void testGetIdToken_notSuccessful() {
        mockNotSuccessfulTokenResult(new IllegalStateException());
        when(mockFirebaseUser.getIdToken(true))
                .thenReturn(mockGetTokenTaskResult);

        TestObserver<String> obs = TestObserver.create();

        RxFirebaseUser.getIdToken(mockFirebaseUser, true)
                .subscribe(obs);

        callOnComplete(mockGetTokenTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockGetTokenTaskResult);

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testLinkWithCredential() {
        mockSuccessfulAuthResult();
        when(mockFirebaseUser.linkWithCredential(mockAuthCredential))
                .thenReturn(mockAuthTaskResult);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseUser.linkWithCredential(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testLinkWithCredential_notSuccessful() {
        mockNotSuccessfulAuthResult(new IllegalStateException());
        when(mockFirebaseUser.linkWithCredential(mockAuthCredential))
                .thenReturn(mockAuthTaskResult);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseUser.linkWithCredential(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testReauthenticate() {
        mockSuccessfulVoidResult();
        when(mockFirebaseUser.reauthenticate(mockAuthCredential))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.reauthenticate(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockVoidTaskResult);

        obs.assertComplete();
    }

    @Test
    public void testReauthenticate_notSuccessful() {
        mockNotSuccessfulVoidResult(new IllegalStateException());
        when(mockFirebaseUser.reauthenticate(mockAuthCredential))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.reauthenticate(mockFirebaseUser, mockAuthCredential)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testReload() {
        mockSuccessfulVoidResult();
        when(mockFirebaseUser.reload())
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.reload(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertComplete();
    }

    @Test
    public void testReload_notSuccessful() {
        mockNotSuccessfulVoidResult(new IllegalStateException());
        when(mockFirebaseUser.reload())
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.reload(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testSendEmailVerification() {
        mockSuccessfulVoidResult();
        when(mockFirebaseUser.sendEmailVerification())
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.sendEmailVerification(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertComplete();
    }

    @Test
    public void testSendEmailVerification_notSuccessful() {
        mockNotSuccessfulVoidResult(new IllegalStateException());
        when(mockFirebaseUser.sendEmailVerification())
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.sendEmailVerification(mockFirebaseUser)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testUnlink() {
        mockSuccessfulAuthResult();
        when(mockFirebaseUser.unlink("provider"))
                .thenReturn(mockAuthTaskResult);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseUser.unlink(mockFirebaseUser, "provider")
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        // Ensure no more values are emitted after unsubscribe
        callOnComplete(mockAuthTaskResult);

        obs.assertComplete();
        obs.assertValueCount(1);
    }

    @Test
    public void testUnlink_notSuccessful() {
        mockNotSuccessfulAuthResult(new IllegalStateException());
        when(mockFirebaseUser.unlink("provider"))
                .thenReturn(mockAuthTaskResult);

        TestObserver<FirebaseUser> obs = TestObserver.create();

        RxFirebaseUser.unlink(mockFirebaseUser, "provider")
                .subscribe(obs);

        callOnComplete(mockAuthTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testUpdateEmail() {
        mockSuccessfulVoidResult();
        when(mockFirebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.updateEmail(mockFirebaseUser, "foo@bar.com")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertComplete();
    }

    @Test
    public void testUpdateEmail_notSuccessful() {
        mockNotSuccessfulVoidResult(new IllegalStateException());
        when(mockFirebaseUser.updateEmail("foo@bar.com"))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.updateEmail(mockFirebaseUser, "foo@bar.com")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testUpdatePassword() {
        mockSuccessfulVoidResult();
        when(mockFirebaseUser.updatePassword("password"))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.updatePassword(mockFirebaseUser, "password")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertComplete();
    }

    @Test
    public void testUpdatePassword_notSuccessful() {
        mockNotSuccessfulVoidResult(new IllegalStateException());
        when(mockFirebaseUser.updatePassword("password"))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.updatePassword(mockFirebaseUser, "password")
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    @Test
    public void testUpdateProfile() {
        mockSuccessfulVoidResult();
        when(mockFirebaseUser.updateProfile(mockProfileChangeRequest))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.updateProfile(mockFirebaseUser, mockProfileChangeRequest)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertComplete();
    }

    @Test
    public void testUpdateProfile_notSuccessful() {
        mockNotSuccessfulVoidResult(new IllegalStateException());
        when(mockFirebaseUser.updateProfile(mockProfileChangeRequest))
                .thenReturn(mockVoidTaskResult);

        TestObserver obs = TestObserver.create();

        RxFirebaseUser.updateProfile(mockFirebaseUser, mockProfileChangeRequest)
                .subscribe(obs);

        callOnComplete(mockVoidTaskResult);
        obs.dispose();

        obs.assertError(IllegalStateException.class);
    }

    private void mockSuccessfulVoidResult() {
        mockVoidResult(true, null);
    }

    private void mockNotSuccessfulVoidResult(@NonNull Exception exception) {
        mockVoidResult(false, exception);
    }

    private void mockVoidResult(boolean success, @Nullable Exception exception) {
        when(mockVoidTaskResult.isSuccessful())
                .thenReturn(success);

        if (null != exception) {
            when(mockVoidTaskResult.getException())
                    .thenReturn(exception);
        }

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

        when(mockAuthResult.getUser())
                .thenReturn(mockFirebaseUser);

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
