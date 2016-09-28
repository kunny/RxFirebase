package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import com.androidhuman.rxfirebase.common.model.TaskResult;
import com.memoizrlabs.retrooptional.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RxFirebaseDatabaseTest {

    @Mock
    DatabaseReference mockDatabaseReference;

    @Mock
    DataSnapshot mockDataSnapshot;

    @Mock
    DatabaseError mockDatabaseError;

    @Mock
    MutableData mockMutableData;

    @Mock
    Func1<MutableData, Transaction.Result> mockTransactionTask;

    private ArgumentCaptor<ValueEventListener> valueEventListener;

    private ArgumentCaptor<Transaction.Handler> transactionHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        valueEventListener = ArgumentCaptor.forClass(ValueEventListener.class);
        transactionHandler = ArgumentCaptor.forClass(Transaction.Handler.class);
    }

    @Test
    public void testData() {
        TestSubscriber<DataSnapshot> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.data(mockDatabaseReference)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");
        s.unsubscribe();

        // Ensure no more values are emitted after unsubscribe
        callValueEventOnDataChange("Foo");

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);
    }

    @Test
    public void testData_onCancelled() {
        TestSubscriber<DataSnapshot> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.data(mockDatabaseReference)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnCancelled(new DatabaseException("foo"));

        sub.assertError(DatabaseException.class);
        sub.assertNoValues();

        s.unsubscribe();

        callValueEventOnCancelled(new DatabaseException("foo"));

        // Assert no more values are emitted after unsubscribe
        assertThat(sub.getOnErrorEvents())
                .hasSize(1);

    }

    @Test
    public void testDataChanges() {
        TestSubscriber<DataSnapshot> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataChanges(mockDatabaseReference)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotCompleted();
        sub.assertValueCount(1);

        s.unsubscribe();

        callValueEventOnDataChange("Foo");

        // Assert no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataChanges_onCancelled() {
        TestSubscriber<DataSnapshot> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataChanges(mockDatabaseReference)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnCancelled(new DatabaseException("foo"));

        sub.assertError(DatabaseException.class);
        sub.assertNoValues();

        s.unsubscribe();

        callValueEventOnCancelled(new DatabaseException("foo"));

        // Assert no more values are emitted after unsubscribe
        assertThat(sub.getOnErrorEvents())
                .hasSize(1);
    }

    @Test
    public void testDataChangesOfClazz() {
        TestSubscriber<Optional<String>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotCompleted();
        sub.assertValueCount(1);

        Optional<String> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();

        assertThat(value.get())
                .isEqualTo("Foo");

        s.unsubscribe();

        callValueEventOnDataChange("Foo");

        // Assert no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataChangesOfGenericTypeIndicator() {
        List<String> values = new ArrayList<>();
        values.add("Foo");
        values.add("Bar");

        GenericTypeIndicator<List<String>> typeIndicator =
                new GenericTypeIndicator<List<String>>() {
                };

        TestSubscriber<Optional<List<String>>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertNotCompleted();
        sub.assertValueCount(1);

        Optional<List<String>> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();

        assertThat(value.get())
                .containsExactly("Foo", "Bar");

        s.unsubscribe();

        callValueEventOnDataChange(typeIndicator, values);

        // Assert no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfClazz() {
        TestSubscriber<Optional<String>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");

        sub.assertCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        Optional<String> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();

        assertThat(value.get())
                .isEqualTo("Foo");

        s.unsubscribe();

        callValueEventOnDataChange("Foo");

        // Assert no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfGenericTypeIndicator() {
        List<String> values = new ArrayList<>();
        values.add("Foo");
        values.add("Bar");

        GenericTypeIndicator<List<String>> typeIndicator =
                new GenericTypeIndicator<List<String>>() {
                };

        TestSubscriber<Optional<List<String>>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        Optional<List<String>> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();

        assertThat(value.get())
                .containsExactly("Foo", "Bar");

        s.unsubscribe();

        callValueEventOnDataChange(typeIndicator, values);

        // Assert no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testRunTransaction() {
        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnComplete();
        verifyTransactionTaskCall();

        sub.assertCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isTrue();

        s.unsubscribe();

        callTransactionOnComplete();

        // Assert no more value are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testRunTransaction_onError() {
        TestSubscriber<TaskResult> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnCompleteWithError(new DatabaseException("Foo"));

        sub.assertCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        TaskResult result = sub.getOnNextEvents().get(0);

        assertThat(result.isSuccess())
                .isFalse();

        assertThat(result.getException())
                .isInstanceOf(DatabaseException.class);

        s.unsubscribe();

        callTransactionOnComplete();

        // Assert no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    private void verifyAddListenerForSingleValueEvent() {
        verify(mockDatabaseReference)
                .addListenerForSingleValueEvent(valueEventListener.capture());
    }

    private void verifyAddValueEventListener() {
        verify(mockDatabaseReference)
                .addValueEventListener(valueEventListener.capture());
    }

    private void verifyRunTransaction() {
        verify(mockDatabaseReference)
                .runTransaction(transactionHandler.capture(), anyBoolean());
    }

    private void verifyTransactionTaskCall() {
        verify(mockTransactionTask)
                .call(mockMutableData);
    }

    private <T> void callValueEventOnDataChange(T value) {
        when(mockDataSnapshot.getValue(value.getClass()))
                .thenReturn(value);

        valueEventListener.getValue().onDataChange(mockDataSnapshot);
    }

    private <T> void callValueEventOnDataChange(GenericTypeIndicator<T> type, T value) {
        when(mockDataSnapshot.getValue(type))
                .thenReturn(value);

        valueEventListener.getValue().onDataChange(mockDataSnapshot);
    }

    private void callValueEventOnCancelled(DatabaseException mockedException) {
        when(mockDatabaseError.toException())
                .thenReturn(mockedException);

        valueEventListener.getValue().onCancelled(mockDatabaseError);
    }

    private void callTransactionOnComplete() {
        transactionHandler.getValue().doTransaction(mockMutableData);
        transactionHandler.getValue().onComplete(null, true, mockDataSnapshot);
    }

    private void callTransactionOnCompleteWithError(DatabaseException mockedException) {
        when(mockDatabaseError.toException())
                .thenReturn(mockedException);

        transactionHandler.getValue().onComplete(mockDatabaseError, false, mockDataSnapshot);
    }
}
