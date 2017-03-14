package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import com.androidhuman.rxfirebase.database.model.DataValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
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

    @Mock
    Task<Void> mockTask;

    private ArgumentCaptor<ChildEventListener> childEventListener;

    private ArgumentCaptor<ValueEventListener> valueEventListener;

    private ArgumentCaptor<Transaction.Handler> transactionHandler;

    private ArgumentCaptor<OnCompleteListener> onCompleteListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        childEventListener = ArgumentCaptor.forClass(ChildEventListener.class);
        valueEventListener = ArgumentCaptor.forClass(ValueEventListener.class);
        transactionHandler = ArgumentCaptor.forClass(Transaction.Handler.class);
        onCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
    }

    @Test
    public void testChildEvents_add() {
        TestSubscriber<ChildEvent> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildAdded("foo");
        callOnChildAdded("foo");

        sub.assertNotCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List<ChildEvent> events = sub.getOnNextEvents();
        for (ChildEvent event : events) {
            assertThat(event)
                    .isInstanceOf(ChildAddEvent.class);
            assertThat(((ChildAddEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        s.unsubscribe();

        callOnChildAdded("baz");

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_change() {
        TestSubscriber<ChildEvent> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildChanged("foo");
        callOnChildChanged("foo");

        sub.assertNotCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List<ChildEvent> events = sub.getOnNextEvents();
        for (ChildEvent event : events) {
            assertThat(event)
                    .isInstanceOf(ChildChangeEvent.class);
            assertThat(((ChildChangeEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        s.unsubscribe();

        callOnChildChanged("foo");

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_remove() {
        TestSubscriber<ChildEvent> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildRemoved();
        callOnChildRemoved();

        sub.assertNotCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List<ChildEvent> events = sub.getOnNextEvents();
        for (ChildEvent event : events) {
            assertThat(event)
                    .isInstanceOf(ChildRemoveEvent.class);
        }

        s.unsubscribe();

        callOnChildRemoved();

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_move() {
        TestSubscriber<ChildEvent> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildMoved("foo");
        callOnChildMoved("foo");

        sub.assertNotCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List<ChildEvent> events = sub.getOnNextEvents();
        for (ChildEvent event : events) {
            assertThat(event)
                    .isInstanceOf(ChildMoveEvent.class);
            assertThat(((ChildMoveEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        s.unsubscribe();

        callOnChildMoved("foo");

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_notSuccessful() {
        TestSubscriber<ChildEvent> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callChildOnCancelled();

        sub.assertNoValues();
        assertThat(sub.getOnErrorEvents())
                .hasSize(1);

        s.unsubscribe();

        callChildOnCancelled();

        // Ensure no more values are emitted after unsubscribe
        assertThat(sub.getOnErrorEvents())
                .hasSize(1);
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

        // Ensure no more values are emitted after unsubscribe
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

        // Ensure no more values are emitted after unsubscribe
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

        // Ensure no more values are emitted after unsubscribe
        assertThat(sub.getOnErrorEvents())
                .hasSize(1);
    }

    @Test
    public void testDataChangesOfClazz() {
        TestSubscriber<DataValue<String>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotCompleted();
        sub.assertValueCount(1);

        DataValue<String> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();
        assertThat(value.get())
                .isEqualTo("Foo");

        s.unsubscribe();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
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

        TestSubscriber<DataValue<List<String>>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertNotCompleted();
        sub.assertValueCount(1);

        DataValue<List<String>> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();
        assertThat(value.get())
                .containsExactly("Foo", "Bar");

        s.unsubscribe();

        callValueEventOnDataChange(typeIndicator, values);

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfClazz() {
        TestSubscriber<DataValue<String>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");

        sub.assertCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        DataValue<String> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();
        assertThat(value.get())
                .isEqualTo("Foo");

        s.unsubscribe();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
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

        TestSubscriber<DataValue<List<String>>> sub = new TestSubscriber<>();

        Subscription s = RxFirebaseDatabase.dataOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertCompleted();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        DataValue<List<String>> value = sub.getOnNextEvents().get(0);

        assertThat(value.isPresent())
                .isTrue();
        assertThat(value.get())
                .containsExactly("Foo", "Bar");

        s.unsubscribe();

        callValueEventOnDataChange(typeIndicator, values);

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testRemoveValue() {
        when(mockDatabaseReference.removeValue())
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.removeValue(mockDatabaseReference)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertCompleted();
    }

    @Test
    public void testRemoveValue_Unsuccessful() {
        when(mockDatabaseReference.removeValue())
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.removeValue(mockDatabaseReference)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testSetPriority() {
        when(mockDatabaseReference.setPriority(1))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.setPriority(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertCompleted();
    }

    @Test
    public void testSetPriority_notSuccessful() {
        when(mockDatabaseReference.setPriority(1))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.setPriority(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testSetValue() {
        when(mockDatabaseReference.setValue(1))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertCompleted();
    }

    @Test
    public void testSetValue_notSuccessful() {
        when(mockDatabaseReference.setValue(1))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testSetValueWithPriority() {
        when(mockDatabaseReference.setValue(1, 1))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertCompleted();
    }

    @Test
    public void testSetValueWithPriority_notSuccessful() {
        when(mockDatabaseReference.setValue(1, 1))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testUpdateChildren() {
        Map<String, Object> map = new HashMap<>();

        when(mockDatabaseReference.updateChildren(map))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.updateChildren(mockDatabaseReference, map)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertCompleted();
    }

    @Test
    public void testUpdateChildren_notSuccessful() {
        Map<String, Object> map = new HashMap<>();

        when(mockDatabaseReference.updateChildren(map))
                .thenReturn(mockTask);

        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase.updateChildren(mockDatabaseReference, map)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testRunTransaction() {
        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnComplete();
        verifyTransactionTaskCall();

        sub.assertCompleted();
    }

    @Test
    public void testRunTransaction_onError() {
        TestSubscriber sub = new TestSubscriber<>();

        RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnCompleteWithError(new DatabaseException("Foo"));

        sub.assertError(DatabaseException.class);
    }

    private void verifyAddChildEventListener() {
        verify(mockDatabaseReference)
                .addChildEventListener(childEventListener.capture());
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

    private void verifyAddOnCompleteListenerForTask() {
        //noinspection unchecked
        verify(mockTask)
                .addOnCompleteListener(onCompleteListener.capture());
    }

    private void callOnChildAdded(String previousChildName) {
        childEventListener.getValue().onChildAdded(mockDataSnapshot, previousChildName);
    }

    private void callOnChildChanged(String previousChildName) {
        childEventListener.getValue().onChildChanged(mockDataSnapshot, previousChildName);
    }

    private void callOnChildMoved(String previousChildName) {
        childEventListener.getValue().onChildMoved(mockDataSnapshot, previousChildName);
    }

    private void callOnChildRemoved() {
        childEventListener.getValue().onChildRemoved(mockDataSnapshot);
    }

    private void callChildOnCancelled() {
        childEventListener.getValue().onCancelled(mockDatabaseError);
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

    private void callTaskOnComplete() {
        when(mockTask.isSuccessful())
                .thenReturn(true);

        //noinspection unchecked
        onCompleteListener.getValue().onComplete(mockTask);
    }

    private void callTaskOnCompleteWithError(Exception e) {
        when(mockTask.isSuccessful())
                .thenReturn(false);

        when(mockTask.getException())
                .thenReturn(e);

        //noinspection unchecked
        onCompleteListener.getValue().onComplete(mockTask);
    }
}
