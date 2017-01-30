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

import com.androidhuman.rxfirebase.common.model.TaskResult;
import com.memoizrlabs.retrooptional.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

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
    Function<MutableData, Transaction.Result> mockTransactionTask;

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
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildAdded("foo");
        callOnChildAdded("foo");

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event).isInstanceOf(ChildEvent.class);
            assertThat(event)
                    .isInstanceOf(ChildAddEvent.class);
            assertThat(((ChildAddEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        sub.dispose();

        callOnChildAdded("baz");

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_change() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildChanged("foo");
        callOnChildChanged("foo");

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event).isInstanceOf(ChildEvent.class);
            assertThat(event)
                    .isInstanceOf(ChildChangeEvent.class);
            assertThat(((ChildChangeEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        sub.dispose();

        callOnChildChanged("foo");

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_remove() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildRemoved();
        callOnChildRemoved();

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event).isInstanceOf(ChildEvent.class);
            assertThat(event)
                    .isInstanceOf(ChildRemoveEvent.class);
        }

        sub.dispose();

        callOnChildRemoved();

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_move() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callOnChildMoved("foo");
        callOnChildMoved("foo");

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event).isInstanceOf(ChildEvent.class);
            assertThat(event)
                    .isInstanceOf(ChildMoveEvent.class);
            assertThat(((ChildMoveEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        sub.dispose();

        callOnChildMoved("foo");

        // Ensure no more values are emitted after unsubscrube
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_notSuccessful() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyAddChildEventListener();
        callChildOnCancelled();

        sub.assertNoValues();
        assertThat(sub.errorCount()).isEqualTo(1);

        sub.dispose();

        callChildOnCancelled();

        // Ensure no more values are emitted after unsubscribe
        assertThat(sub.errorCount())
                .isEqualTo(1);
    }

    @Test
    public void testData() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.data(mockDatabaseReference)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");
        sub.dispose();

        // Ensure no more values are emitted after unsubscribe
        callValueEventOnDataChange("Foo");

        sub.assertNoErrors();
        sub.assertComplete();
        sub.assertValueCount(1);
    }

    @Test
    public void testData_onCancelled() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.data(mockDatabaseReference)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnCancelled(new DatabaseException("foo"));

        sub.assertError(DatabaseException.class);
        sub.assertNoValues();

        sub.dispose();

        callValueEventOnCancelled(new DatabaseException("foo"));

        // Ensure no more values are emitted after unsubscribe
        assertThat(sub.errorCount())
                .isEqualTo(1);

    }

    @Test
    public void testDataChanges() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.dataChanges(mockDatabaseReference)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotComplete();
        sub.assertValueCount(1);

        sub.dispose();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataChanges_onCancelled() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.dataChanges(mockDatabaseReference)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnCancelled(new DatabaseException("foo"));

        sub.assertError(DatabaseException.class);
        sub.assertNoValues();

        sub.dispose();

        callValueEventOnCancelled(new DatabaseException("foo"));

        // Ensure no more values are emitted after unsubscribe
        assertThat(sub.errorCount())
                .isEqualTo(1);
    }

    @Test
    public void testDataChangesOfClazz() {
        TestObserver<Optional<String>> sub = TestObserver.create();

        RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotComplete();
        sub.assertValueCount(1);
        sub.assertValue(new Predicate<Optional<String>>() {
            @Override
            public boolean test(Optional<String> stringOptional) throws Exception {
                return stringOptional.isPresent() && "Foo".equals(stringOptional.get());

            }
        });

        sub.dispose();

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

        TestObserver<Optional<List<String>>> sub = TestObserver.create();

        RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyAddValueEventListener();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertNotComplete();

        sub.assertValue(new Predicate<Optional<List<String>>>() {
            @Override
            public boolean test(Optional<List<String>> listOptional) throws Exception {
                return listOptional.isPresent()
                        && "Foo".equals(listOptional.get().get(0))
                        && "Bar".equals(listOptional.get().get(1));
            }
        });

        sub.dispose();

        callValueEventOnDataChange(typeIndicator, values);

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfClazz() {
        TestObserver<Optional<String>> sub = TestObserver.create();

        RxFirebaseDatabase.dataOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(new Predicate<Optional<String>>() {
            @Override
            public boolean test(Optional<String> stringOptional) throws Exception {
                return stringOptional.isPresent() && "Foo".equals(stringOptional.get());
            }
        });

        sub.dispose();

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

        TestObserver<Optional<List<String>>> sub = TestObserver.create();

        RxFirebaseDatabase.dataOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyAddListenerForSingleValueEvent();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(new Predicate<Optional<List<String>>>() {
            @Override
            public boolean test(Optional<List<String>> listOptional) throws Exception {
                return listOptional.isPresent()
                        && "Foo".equals(listOptional.get().get(0))
                        && "Bar".equals(listOptional.get().get(1));
            }
        });

        sub.dispose();

        callValueEventOnDataChange(typeIndicator, values);

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testRemoveValue() {
        when(mockDatabaseReference.removeValue())
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.removeValue(mockDatabaseReference)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskSuccess());

        sub.dispose();

        callTaskOnComplete();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testRemoveValue_Unsuccessful() {
        when(mockDatabaseReference.removeValue())
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.removeValue(mockDatabaseReference)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskError(IllegalStateException.class));

        sub.dispose();

        callTaskOnCompleteWithError(new IllegalStateException());

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testSetPriority() {
        when(mockDatabaseReference.setPriority(1))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.setPriority(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        sub.assertValue(assertTaskSuccess());

        sub.dispose();

        callTaskOnComplete();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testSetPriority_notSuccessful() {
        when(mockDatabaseReference.setPriority(1))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.setPriority(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskError(IllegalStateException.class));

        sub.dispose();

        callTaskOnCompleteWithError(new IllegalStateException());

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testSetValue() {
        when(mockDatabaseReference.setValue(1))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        sub.assertValue(new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return taskResult.isSuccess();
            }
        });

        sub.dispose();

        callTaskOnComplete();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testSetValue_notSuccessful() {
        when(mockDatabaseReference.setValue(1))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskError(IllegalStateException.class));

        sub.dispose();

        callTaskOnCompleteWithError(new IllegalStateException());

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testSetValueWithPriority() {
        when(mockDatabaseReference.setValue(1, 1))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskSuccess());

        sub.dispose();

        callTaskOnComplete();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testSetValueWithPriority_notSuccessful() {
        when(mockDatabaseReference.setValue(1, 1))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertComplete();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        sub.assertValue(assertTaskError(IllegalStateException.class));

        sub.dispose();

        callTaskOnCompleteWithError(new IllegalStateException());

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testUpdateChildren() {
        Map<String, Object> map = new HashMap<>();

        when(mockDatabaseReference.updateChildren(map))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.updateChildren(mockDatabaseReference, map)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();
        sub.assertValueCount(1);

        sub.assertValue(assertTaskSuccess());

        sub.dispose();

        callTaskOnComplete();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testUpdateChildren_notSuccessful() {
        Map<String, Object> map = new HashMap<>();

        when(mockDatabaseReference.updateChildren(map))
                .thenReturn(mockTask);

        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase.updateChildren(mockDatabaseReference, map)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskError(IllegalStateException.class));

        sub.dispose();

        callTaskOnCompleteWithError(new IllegalStateException());

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testRunTransaction() throws Exception {
        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnComplete();
        verifyTransactionTaskCall();

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskSuccess());

        sub.dispose();

        callTransactionOnComplete();

        // Ensure no more value are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testRunTransaction_onError() {
        TestObserver<TaskResult> sub = TestObserver.create();

        RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnCompleteWithError(new DatabaseException("Foo"));

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(assertTaskError(DatabaseException.class));

        sub.dispose();

        callTransactionOnComplete();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
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

    private void verifyTransactionTaskCall() throws Exception {
        verify(mockTransactionTask)
                .apply(mockMutableData);
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

    private Predicate<TaskResult> assertTaskSuccess() {
        return new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return taskResult.isSuccess();
            }
        };
    }

    private Predicate<TaskResult> assertTaskError(
            @NonNull final Class<? extends Throwable> errorClass) {
        return new Predicate<TaskResult>() {
            @Override
            public boolean test(TaskResult taskResult) throws Exception {
                return !taskResult.isSuccess() && errorClass
                        .isAssignableFrom(taskResult.getException().getClass());
            }
        };
    }
}
