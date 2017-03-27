package com.androidhuman.rxfirebase2.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import com.androidhuman.rxfirebase2.database.model.DataValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    Query mockQuery;

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
    public void testChildEvents_DataReference_add() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddChildEventListener();
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

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_DataReference_change() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddChildEventListener();
        callOnChildChanged("foo");
        callOnChildChanged("foo");

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event)
                    .isInstanceOf(ChildChangeEvent.class);
            assertThat(((ChildChangeEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        sub.dispose();

        callOnChildChanged("foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_DataReference_remove() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddChildEventListener();
        callOnChildRemoved();
        callOnChildRemoved();

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event)
                    .isInstanceOf(ChildRemoveEvent.class);
        }

        sub.dispose();

        callOnChildRemoved();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_DataReference_move() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddChildEventListener();
        callOnChildMoved("foo");
        callOnChildMoved("foo");

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event)
                    .isInstanceOf(ChildMoveEvent.class);
            assertThat(((ChildMoveEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        sub.dispose();

        callOnChildMoved("foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_DataReference_notSuccessful() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddChildEventListener();
        callChildOnCancelled();

        sub.assertNoValues();
        assertThat(sub.errorCount())
                .isEqualTo(1);

        sub.dispose();

        callChildOnCancelled();

        // Ensure no more values are emitted after unsubscribe
        assertThat(sub.errorCount())
                .isEqualTo(1);
    }

    @Test
    public void testData_DataReference() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.data(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");
        sub.dispose();

        // Ensure no more values are emitted after unsubscribe
        callValueEventOnDataChange("Foo");

        sub.assertNoErrors();
        sub.assertComplete();
        sub.assertValueCount(1);
    }

    @Test
    public void testData_DataReference_onCancelled() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.data(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddListenerForSingleValueEvent();
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
    public void testDataChanges_DataReference() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.dataChanges(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotComplete();
        sub.assertValueCount(1);

        sub.dispose();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataChanges_DataReference_onCancelled() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.dataChanges(mockDatabaseReference)
                .subscribe(sub);

        verifyDataReferenceAddValueEventListener();
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
    public void testDataChangesOfClazz_DataReference() {
        TestObserver<DataValue<String>> sub = TestObserver.create();

        RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyDataReferenceAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotComplete();

        sub.assertValue(new Predicate<DataValue<String>>() {
            @Override
            public boolean test(DataValue<String> value) throws Exception {
                return value.isPresent() && "Foo".equals(value.get());
            }
        });

        sub.dispose();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataChangesOfGenericTypeIndicator_DataReference() {
        List<String> values = new ArrayList<>();
        values.add("Foo");
        values.add("Bar");

        GenericTypeIndicator<List<String>> typeIndicator =
                new GenericTypeIndicator<List<String>>() {
                };

        TestObserver<DataValue<List<String>>> sub = TestObserver.create();

        RxFirebaseDatabase.dataChangesOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyDataReferenceAddValueEventListener();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertNotComplete();

        sub.assertValue(new Predicate<DataValue<List<String>>>() {
            @Override
            public boolean test(DataValue<List<String>> value) throws Exception {
                return value.isPresent()
                        && "Foo".equals(value.get().get(0))
                        && "Bar".equals(value.get().get(1));
            }
        });

        sub.dispose();

        callValueEventOnDataChange(typeIndicator, values);

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfClazz_DataReference() {
        TestObserver<String> sub = TestObserver.create();

        RxFirebaseDatabase.dataOf(mockDatabaseReference, String.class)
                .subscribe(sub);

        verifyDataReferenceAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue("Foo");

        sub.dispose();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfGenericTypeIndicator_DataReference() {
        List<String> values = new ArrayList<>();
        values.add("Foo");
        values.add("Bar");

        GenericTypeIndicator<List<String>> typeIndicator =
                new GenericTypeIndicator<List<String>>() {
                };

        TestObserver<List<String>> sub = TestObserver.create();

        RxFirebaseDatabase.dataOf(mockDatabaseReference, typeIndicator)
                .subscribe(sub);

        verifyDataReferenceAddListenerForSingleValueEvent();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(new Predicate<List<String>>() {
            @Override
            public boolean test(List<String> value) throws Exception {
                return "Foo".equals(value.get(0))
                        && "Bar".equals(value.get(1));
            }
        });

        sub.dispose();

        callValueEventOnDataChange(typeIndicator, values);

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testChildEvents_Query_add() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockQuery)
                .subscribe(sub);

        verifyQueryAddChildEventListener();
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

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_Query_change() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockQuery)
                .subscribe(sub);

        verifyQueryAddChildEventListener();
        callOnChildChanged("foo");
        callOnChildChanged("foo");

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event)
                    .isInstanceOf(ChildChangeEvent.class);
            assertThat(((ChildChangeEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        sub.dispose();

        callOnChildChanged("foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_Query_remove() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockQuery)
                .subscribe(sub);

        verifyQueryAddChildEventListener();
        callOnChildRemoved();
        callOnChildRemoved();

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event)
                    .isInstanceOf(ChildRemoveEvent.class);
        }

        sub.dispose();

        callOnChildRemoved();

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_Query_move() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockQuery)
                .subscribe(sub);

        verifyQueryAddChildEventListener();
        callOnChildMoved("foo");
        callOnChildMoved("foo");

        sub.assertNotComplete();
        sub.assertNoErrors();
        sub.assertValueCount(2);

        List events = sub.getEvents().get(0);
        for (Object event : events) {
            assertThat(event)
                    .isInstanceOf(ChildMoveEvent.class);
            assertThat(((ChildMoveEvent) event).previousChildName())
                    .isEqualTo("foo");
        }

        sub.dispose();

        callOnChildMoved("foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(2);
    }

    @Test
    public void testChildEvents_Query_notSuccessful() {
        TestObserver<ChildEvent> sub = TestObserver.create();

        RxFirebaseDatabase.childEvents(mockQuery)
                .subscribe(sub);

        verifyQueryAddChildEventListener();
        callChildOnCancelled();

        sub.assertNoValues();
        assertThat(sub.errorCount())
                .isEqualTo(1);

        sub.dispose();

        callChildOnCancelled();

        // Ensure no more values are emitted after unsubscribe
        assertThat(sub.errorCount())
                .isEqualTo(1);
    }

    @Test
    public void testData_Query() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.data(mockQuery)
                .subscribe(sub);

        verifyQueryAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");
        sub.dispose();

        // Ensure no more values are emitted after unsubscribe
        callValueEventOnDataChange("Foo");

        sub.assertNoErrors();
        sub.assertComplete();
        sub.assertValueCount(1);
    }

    @Test
    public void testData_Query_onCancelled() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.data(mockQuery)
                .subscribe(sub);

        verifyQueryAddListenerForSingleValueEvent();
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
    public void testDataChanges_Query() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.dataChanges(mockQuery)
                .subscribe(sub);

        verifyQueryAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotComplete();
        sub.assertValueCount(1);

        sub.dispose();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataChanges_Query_onCancelled() {
        TestObserver<DataSnapshot> sub = TestObserver.create();

        RxFirebaseDatabase.dataChanges(mockQuery)
                .subscribe(sub);

        verifyQueryAddValueEventListener();
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
    public void testDataChangesOfClazz_Query() {
        TestObserver<DataValue<String>> sub = TestObserver.create();

        RxFirebaseDatabase.dataChangesOf(mockQuery, String.class)
                .subscribe(sub);

        verifyQueryAddValueEventListener();
        callValueEventOnDataChange("Foo");

        sub.assertNotComplete();

        sub.assertValue(new Predicate<DataValue<String>>() {
            @Override
            public boolean test(DataValue<String> value) throws Exception {
                return value.isPresent() && "Foo".equals(value.get());
            }
        });

        sub.dispose();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataChangesOfGenericTypeIndicator_Query() {
        List<String> values = new ArrayList<>();
        values.add("Foo");
        values.add("Bar");

        GenericTypeIndicator<List<String>> typeIndicator =
                new GenericTypeIndicator<List<String>>() {
                };

        TestObserver<DataValue<List<String>>> sub = TestObserver.create();

        RxFirebaseDatabase.dataChangesOf(mockQuery, typeIndicator)
                .subscribe(sub);

        verifyQueryAddValueEventListener();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertNotComplete();

        sub.assertValue(new Predicate<DataValue<List<String>>>() {
            @Override
            public boolean test(DataValue<List<String>> value) throws Exception {
                return value.isPresent()
                        && "Foo".equals(value.get().get(0))
                        && "Bar".equals(value.get().get(1));
            }
        });

        sub.dispose();

        callValueEventOnDataChange(typeIndicator, values);

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfClazz_Query() {
        TestObserver<String> sub = TestObserver.create();

        RxFirebaseDatabase.dataOf(mockQuery, String.class)
                .subscribe(sub);

        verifyQueryAddListenerForSingleValueEvent();
        callValueEventOnDataChange("Foo");

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue("Foo");

        sub.dispose();

        callValueEventOnDataChange("Foo");

        // Ensure no more values are emitted after unsubscribe
        sub.assertValueCount(1);
    }

    @Test
    public void testDataOfGenericTypeIndicator_Query() {
        List<String> values = new ArrayList<>();
        values.add("Foo");
        values.add("Bar");

        GenericTypeIndicator<List<String>> typeIndicator =
                new GenericTypeIndicator<List<String>>() {
                };

        TestObserver<List<String>> sub = TestObserver.create();

        RxFirebaseDatabase.dataOf(mockQuery, typeIndicator)
                .subscribe(sub);

        verifyQueryAddListenerForSingleValueEvent();
        callValueEventOnDataChange(typeIndicator, values);

        sub.assertComplete();
        sub.assertNoErrors();

        sub.assertValue(new Predicate<List<String>>() {
            @Override
            public boolean test(List<String> value) throws Exception {
                return "Foo".equals(value.get(0))
                        && "Bar".equals(value.get(1));
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

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.removeValue(mockDatabaseReference)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();

        sub.dispose();
    }

    @Test
    public void testRemoveValue_Unsuccessful() {
        when(mockDatabaseReference.removeValue())
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.removeValue(mockDatabaseReference)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertNotComplete();
        sub.assertError(IllegalStateException.class);

        sub.dispose();
    }

    @Test
    public void testSetPriority() {
        when(mockDatabaseReference.setPriority(1))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.setPriority(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();

        sub.dispose();
    }

    @Test
    public void testSetPriority_notSuccessful() {
        when(mockDatabaseReference.setPriority(1))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.setPriority(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertNotComplete();
        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testSetValue() {
        when(mockDatabaseReference.setValue(1))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();
        sub.dispose();
    }

    @Test
    public void testSetValue_notSuccessful() {
        when(mockDatabaseReference.setValue(1))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertNotComplete();
        sub.assertError(IllegalStateException.class);
    }

    @Test
    public void testSetValueWithPriority() {
        when(mockDatabaseReference.setValue(1, 1))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();
        sub.dispose();
    }

    @Test
    public void testSetValueWithPriority_notSuccessful() {
        when(mockDatabaseReference.setValue(1, 1))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.setValue(mockDatabaseReference, 1, 1)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertNotComplete();
        sub.assertError(IllegalStateException.class);
        sub.dispose();
    }

    @Test
    public void testUpdateChildren() {
        Map<String, Object> map = new HashMap<>();

        when(mockDatabaseReference.updateChildren(map))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.updateChildren(mockDatabaseReference, map)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnComplete();

        sub.assertComplete();
        sub.assertNoErrors();
        sub.dispose();
    }

    @Test
    public void testUpdateChildren_notSuccessful() {
        Map<String, Object> map = new HashMap<>();

        when(mockDatabaseReference.updateChildren(map))
                .thenReturn(mockTask);

        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase.updateChildren(mockDatabaseReference, map)
                .subscribe(sub);

        verifyAddOnCompleteListenerForTask();
        callTaskOnCompleteWithError(new IllegalStateException());

        sub.assertNotComplete();
        sub.assertError(IllegalStateException.class);

        sub.dispose();
    }

    @Test
    public void testRunTransaction() throws Exception {
        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnComplete();
        verifyTransactionTaskCall();

        sub.assertComplete();
        sub.assertNoErrors();

        sub.dispose();
    }

    @Test
    public void testRunTransaction_onError() {
        TestObserver sub = TestObserver.create();

        RxFirebaseDatabase
                .runTransaction(mockDatabaseReference, mockTransactionTask)
                .subscribe(sub);

        verifyRunTransaction();

        callTransactionOnCompleteWithError(new DatabaseException("Foo"));

        sub.assertNotComplete();
        sub.assertError(DatabaseException.class);

        sub.dispose();
    }

    private void verifyDataReferenceAddChildEventListener() {
        verify(mockDatabaseReference)
                .addChildEventListener(childEventListener.capture());
    }

    private void verifyDataReferenceAddListenerForSingleValueEvent() {
        verify(mockDatabaseReference)
                .addListenerForSingleValueEvent(valueEventListener.capture());
    }

    private void verifyDataReferenceAddValueEventListener() {
        verify(mockDatabaseReference)
                .addValueEventListener(valueEventListener.capture());
    }

    private void verifyQueryAddChildEventListener() {
        verify(mockQuery)
                .addChildEventListener(childEventListener.capture());
    }

    private void verifyQueryAddListenerForSingleValueEvent() {
        verify(mockQuery)
                .addListenerForSingleValueEvent(valueEventListener.capture());
    }

    private void verifyQueryAddValueEventListener() {
        verify(mockQuery)
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
        when(mockDataSnapshot.exists())
                .thenReturn(true);
        when(mockDataSnapshot.getValue(value.getClass()))
                .thenReturn(value);

        valueEventListener.getValue().onDataChange(mockDataSnapshot);
    }

    private <T> void callValueEventOnDataChange(GenericTypeIndicator<T> type, T value) {
        when(mockDataSnapshot.exists())
                .thenReturn(true);
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
