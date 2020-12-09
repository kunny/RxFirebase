package com.androidhuman.rxfirebase3.database

import com.androidhuman.rxfirebase3.database.model.DataValue
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.MutableData
import com.google.firebase.database.Query
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@Suppress("NOTHING_TO_INLINE")
class RxFirebaseDatabaseTest {

    @Mock
    private lateinit var databaseReference: DatabaseReference

    @Mock
    lateinit var query: Query

    private val childEventListener = argumentCaptor<ChildEventListener>()

    private val valueEventListener = argumentCaptor<ValueEventListener>()

    private val transactionHandler = argumentCaptor<Transaction.Handler>()

    private val completionListener = argumentCaptor<DatabaseReference.CompletionListener>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    // Bindings for DatabaseReference

    @Test
    fun dataReferenceFlowableChildEventsAdded() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber.create<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "foo")
            childEventListener.lastValue.onChildAdded(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildAddEvent.create(snapshot, "foo"),
                    ChildAddEvent.create(snapshot, "bar"))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceObservableChildEventsAdded() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver.create<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "foo")
            childEventListener.lastValue.onChildAdded(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildAddEvent.create(snapshot, "foo"),
                    ChildAddEvent.create(snapshot, "bar"))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceFlowableChildEventsChanged() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "foo")
            childEventListener.lastValue.onChildChanged(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildChangeEvent.create(snapshot, "foo"),
                    ChildChangeEvent.create(snapshot, "bar"))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceObservableChildEventsChanged() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "foo")
            childEventListener.lastValue.onChildChanged(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildChangeEvent.create(snapshot, "foo"),
                    ChildChangeEvent.create(snapshot, "bar"))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceFlowableChildEventsRemoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)
            childEventListener.lastValue.onChildRemoved(snapshot)

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildRemoveEvent.create(snapshot),
                    ChildRemoveEvent.create(snapshot))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceObservableChildEventsRemoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)
            childEventListener.lastValue.onChildRemoved(snapshot)

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildRemoveEvent.create(snapshot),
                    ChildRemoveEvent.create(snapshot))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceFlowableChildEventsMoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "foo")
            childEventListener.lastValue.onChildMoved(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildMoveEvent.create(snapshot, "foo"),
                    ChildMoveEvent.create(snapshot, "bar"))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceObservableChildEventsMoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "foo")
            childEventListener.lastValue.onChildMoved(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildMoveEvent.create(snapshot, "foo"),
                    ChildMoveEvent.create(snapshot, "bar"))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceFlowableChildEventsCancelled() {
        val error = databaseError()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            cancel()
        }
    }

    @Test
    fun dataReferenceObservableChildEventsCancelled() {
        val error = databaseError()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(databaseReference)
                    .subscribe(this)

            // verify addChildEventListener() has called
            databaseReference.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun dataReferenceData() {
        val snapshot = dataSnapshot("foo")

        with(TestObserver.create<DataSnapshot>()) {
            RxFirebaseDatabase.data(databaseReference)
                    .subscribe(this)

            // verify addListenerForSingleValueEvent() has called
            databaseReference.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertValue { "foo" == it.value }

            dispose()
        }
    }

    @Test
    fun dataReferenceDataCancelled() {
        val error = databaseError()

        with(TestObserver.create<DataSnapshot>()) {
            RxFirebaseDatabase.data(databaseReference)
                    .subscribe(this)

            // verify addValueForSingleValueEvent() has called
            databaseReference.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun dataReferenceFlowableDataChanges() {
        val snapshot1 = dataSnapshot("foo")
        val snapshot2 = dataSnapshot("bar")

        with(TestSubscriber.create<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(databaseReference, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)
            valueEventListener.lastValue.onDataChange(snapshot2)

            assertNoErrors()
            assertNotComplete()

            assertValueAt(0) { "foo" == it.value }
            assertValueAt(1) { "bar" == it.value }

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)

            // assert no more values
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceObservableDataChanges() {
        val snapshot1 = dataSnapshot("foo")
        val snapshot2 = dataSnapshot("bar")

        with(TestObserver.create<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(databaseReference)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)
            valueEventListener.lastValue.onDataChange(snapshot2)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) { "foo" == it.value }
            assertValueAt(1) { "bar" == it.value }

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)

            // assert no more values
            assertValueCount(2)
        }
    }

    @Test
    fun dataReferenceFlowableDataChangesCancelled() {
        val error = databaseError()

        with(ErrorTestSubscriber<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(databaseReference, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            // assert no more values
            assertThat(errorCount())
                    .isEqualTo(1)
        }
    }

    @Test
    fun dataReferenceObservableDataChangesCancelled() {
        val error = databaseError()

        with(ErrorTestObserver<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(databaseReference)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            // assert no more values
            assertThat(errorCount())
                    .isEqualTo(1)
        }
    }

    @Test
    fun dataReferenceFlowableDataChangesOfClazz() {
        val snapshot = dataSnapshotOfClazz("foo")

        with(TestSubscriber.create<DataValue<String>>()) {
            RxFirebaseDatabase.dataChangesOf(databaseReference,
                    String::class.java, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) { "foo" == it.value() }

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun dataReferenceObservableDataChangesOfClazz() {
        val snapshot = dataSnapshotOfClazz("foo")

        with(TestObserver.create<DataValue<String>>()) {
            RxFirebaseDatabase.dataChangesOf(databaseReference, String::class.java)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) { "foo" == it.value() }

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun dataReferenceFlowableDataChangesOfTypeIndicator() {
        val ti = mock<GenericTypeIndicator<List<String>>>()
        val snapshot = dataSnapshotOfTypeIndicator(listOf("foo", "bar"), ti)

        with(TestSubscriber.create<DataValue<List<String>>>()) {
            RxFirebaseDatabase.dataChangesOf(databaseReference, ti, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()

            assertThat(values()[0].value())
                    .containsExactly("foo", "bar")

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun dataReferenceObservableDataChangesOfTypeIndicator() {
        val ti = mock<GenericTypeIndicator<List<String>>>()
        val snapshot = dataSnapshotOfTypeIndicator(listOf("foo", "bar"), ti)

        with(TestObserver.create<DataValue<List<String>>>()) {
            RxFirebaseDatabase.dataChangesOf(databaseReference, ti)
                    .subscribe(this)

            // verify addValueEventListener() has called
            databaseReference.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()

            assertThat(values()[0].value())
                    .containsExactly("foo", "bar")

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun dataReferenceDataOfClazz() {
        val snapshot = dataSnapshotOfClazz("foo")

        with(TestObserver.create<String>()) {
            RxFirebaseDatabase.dataOf(databaseReference, String::class.java)
                    .subscribe(this)

            // verify addListenerForSingleValueEvent() has called
            databaseReference.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertValue { "foo" == it }

            dispose()
        }
    }

    @Test
    fun dataReferenceDataOfGenericTypeIndicator() {
        val ti = mock<GenericTypeIndicator<List<String>>>()
        val snapshot = dataSnapshotOfTypeIndicator(listOf("foo", "bar"), ti)

        with(TestObserver.create<List<String>>()) {
            RxFirebaseDatabase.dataOf(databaseReference, ti)
                    .subscribe(this)

            // verify addListenerForSingleValueEvent() has called
            databaseReference.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertValueCount(1)
            assertThat(values()[0])
                    .containsExactly("foo", "bar")

            dispose()
        }
    }

    @Test
    fun dataReferenceRemoveValue() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.removeValue(databaseReference)
                    .subscribe(this)

            // verify removeValue() has called
            verify(databaseReference, times(1))
                    .removeValue(completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(null, databaseReference)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun dataReferenceRemoveValueNotSuccessful() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.removeValue(databaseReference)
                    .subscribe(this)

            // verify removeValue() has called
            verify(databaseReference, times(1))
                    .removeValue(completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(databaseError(), databaseReference)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun dataReferenceSetPriority() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.setPriority(databaseReference, 1)
                    .subscribe(this)

            // verify setPriority() has called
            verify(databaseReference, times(1))
                    .setPriority(eq(1), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(null, databaseReference)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun dataReferenceSetPriorityNotSuccessful() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.setPriority(databaseReference, 1)
                    .subscribe(this)

            // verify setPriority() has called
            verify(databaseReference, times(1))
                    .setPriority(eq(1), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(databaseError(), databaseReference)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun dataReferenceSetValue() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.setValue(databaseReference, 100)
                    .subscribe(this)

            // verify setValue() has called
            verify(databaseReference, times(1))
                    .setValue(eq(100), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(null, databaseReference)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun dataReferenceSetValueNotSuccessful() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.setValue(databaseReference, 100)
                    .subscribe(this)

            // verify setValue() has called
            verify(databaseReference, times(1))
                    .setValue(eq(100), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(databaseError(), databaseReference)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun dataReferenceSetValueWithPriority() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.setValue(databaseReference, 100, 1)
                    .subscribe(this)

            // verify setValue() has called
            verify(databaseReference, times(1))
                    .setValue(eq(100), eq(1), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(null, databaseReference)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun dataReferenceSetValueWithPriorityNotSuccessful() {
        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.setValue(databaseReference, 100, 1)
                    .subscribe(this)

            // verify setValue() has called
            verify(databaseReference, times(1))
                    .setValue(eq(100), eq(1), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(databaseError(), databaseReference)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun dataReferenceUpdateChildren() {
        val update = mock<Map<String, Any>>()

        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.updateChildren(databaseReference, update)
                    .subscribe(this)

            // verify updateChildren() has called
            verify(databaseReference, times(1))
                    .updateChildren(eq(update), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(null, databaseReference)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun dataReferenceUpdateChildrenNotSuccessful() {
        val update = mock<Map<String, Any>>()

        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.updateChildren(databaseReference, update)
                    .subscribe(this)

            // verify updateChildren() has called
            verify(databaseReference, times(1))
                    .updateChildren(eq(update), completionListener.capture())

            // simulate the callback
            completionListener.lastValue.onComplete(databaseError(), databaseReference)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun dataReferenceRunTransaction() {
        val snapshot = dataSnapshot("foo")
        val task = mock<Function<MutableData, Transaction.Result>>()

        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.runTransaction(databaseReference, task)
                    .subscribe(this)

            // verify runTransaction() has called
            verify(databaseReference, times(1))
                    .runTransaction(transactionHandler.capture(), eq(true))

            // simulate the callback
            transactionHandler.lastValue.doTransaction(mock())
            transactionHandler.lastValue.onComplete(null, true, snapshot)

            assertNoErrors()
            assertComplete()

            dispose()
        }
    }

    @Test
    fun dataReferenceRunTransactionNotSuccessful() {
        val snapshot = dataSnapshot("foo")
        val task = mock<Function<MutableData, Transaction.Result>>()

        with(TestObserver.create<Any>()) {
            RxFirebaseDatabase.runTransaction(databaseReference, task)
                    .subscribe(this)

            // verify runTransaction() has called
            verify(databaseReference, times(1))
                    .runTransaction(transactionHandler.capture(), eq(true))

            // simulate the callback
            transactionHandler.lastValue.onComplete(databaseError(), true, snapshot)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    // Bindings for Query

    @Test
    fun queryFlowableChildEventsAdded() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber.create<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "foo")
            childEventListener.lastValue.onChildAdded(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildAddEvent.create(snapshot, "foo"),
                    ChildAddEvent.create(snapshot, "bar"))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryObservableChildEventsAdded() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver.create<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "foo")
            childEventListener.lastValue.onChildAdded(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildAddEvent.create(snapshot, "foo"),
                    ChildAddEvent.create(snapshot, "bar"))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildAdded(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryFlowableChildEventsChanged() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "foo")
            childEventListener.lastValue.onChildChanged(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildChangeEvent.create(snapshot, "foo"),
                    ChildChangeEvent.create(snapshot, "bar"))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryObservableChildEventsChanged() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "foo")
            childEventListener.lastValue.onChildChanged(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildChangeEvent.create(snapshot, "foo"),
                    ChildChangeEvent.create(snapshot, "bar"))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildChanged(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryFlowableChildEventsRemoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)
            childEventListener.lastValue.onChildRemoved(snapshot)

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildRemoveEvent.create(snapshot),
                    ChildRemoveEvent.create(snapshot))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryObservableChildEventsRemoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)
            childEventListener.lastValue.onChildRemoved(snapshot)

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildRemoveEvent.create(snapshot),
                    ChildRemoveEvent.create(snapshot))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildRemoved(snapshot)

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryFlowableChildEventsMoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "foo")
            childEventListener.lastValue.onChildMoved(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildMoveEvent.create(snapshot, "foo"),
                    ChildMoveEvent.create(snapshot, "bar"))

            cancel()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryObservableChildEventsMoved() {
        val snapshot = mock<DataSnapshot>()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "foo")
            childEventListener.lastValue.onChildMoved(snapshot, "bar")

            assertNotComplete()
            assertNoErrors()
            assertValues(
                    ChildMoveEvent.create(snapshot, "foo"),
                    ChildMoveEvent.create(snapshot, "bar"))

            dispose()

            // simulate the callback
            childEventListener.lastValue.onChildMoved(snapshot, "baz")

            // assert no more values are emitted
            assertValueCount(2)
        }
    }

    @Test
    fun queryFlowableChildEventsCancelled() {
        val error = databaseError()

        with(TestSubscriber<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            cancel()
        }
    }

    @Test
    fun queryObservableChildEventsCancelled() {
        val error = databaseError()

        with(TestObserver<ChildEvent>()) {
            RxFirebaseDatabase.childEvents(query)
                    .subscribe(this)

            // verify addChildEventListener() has called
            query.verifyAddChildEventListenerCalled()

            // simulate the callback
            childEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun queryData() {
        val snapshot = mock<DataSnapshot>().apply {
            whenever(value)
                    .thenReturn("foo")
        }

        with(TestObserver.create<DataSnapshot>()) {
            RxFirebaseDatabase.data(query)
                    .subscribe(this)

            // verify addListenerForSingleValueEvent() has called
            query.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertValue { "foo" == it.value }

            dispose()
        }
    }

    @Test
    fun queryDataCancelled() {
        val error = databaseError()

        with(TestObserver.create<DataSnapshot>()) {
            RxFirebaseDatabase.data(query)
                    .subscribe(this)

            // verify addListenerForSingleValueEvent() has called
            query.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            dispose()
        }
    }

    @Test
    fun queryFlowableDataChanges() {
        val snapshot1 = dataSnapshot("foo")
        val snapshot2 = dataSnapshot("bar")

        with(TestSubscriber.create<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)
            valueEventListener.lastValue.onDataChange(snapshot2)

            assertNoErrors()
            assertNotComplete()

            assertValueAt(0) { "foo" == it.value }
            assertValueAt(1) { "bar" == it.value }

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)

            // assert no more values
            assertValueCount(2)
        }
    }

    @Test
    fun queryObservableDataChanges() {
        val snapshot1 = dataSnapshot("foo")
        val snapshot2 = dataSnapshot("bar")

        with(TestObserver.create<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(query)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)
            valueEventListener.lastValue.onDataChange(snapshot2)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) { "foo" == it.value }
            assertValueAt(1) { "bar" == it.value }

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot1)

            // assert no more values
            assertValueCount(2)
        }
    }

    @Test
    fun queryFlowableDataChangesCancelled() {
        val error = databaseError()

        with(ErrorTestSubscriber<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            // assert no more values
            assertThat(errorCount())
                    .isEqualTo(1)
        }
    }

    @Test
    fun queryObservableDataChangesCancelled() {
        val error = databaseError()

        with(ErrorTestObserver<DataSnapshot>()) {
            RxFirebaseDatabase.dataChanges(query)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            assertError(DatabaseException::class.java)

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onCancelled(error)

            // assert no more values
            assertThat(errorCount())
                    .isEqualTo(1)
        }
    }

    @Test
    fun queryFlowableDataChangesOfClazz() {
        val snapshot = dataSnapshotOfClazz("foo")

        with(TestSubscriber.create<DataValue<String>>()) {
            RxFirebaseDatabase.dataChangesOf(query,
                    String::class.java, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) { "foo" == it.value() }

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun queryObservableDataChangesOfClazz() {
        val snapshot = dataSnapshotOfClazz("foo")

        with(TestObserver.create<DataValue<String>>()) {
            RxFirebaseDatabase.dataChangesOf(query, String::class.java)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()
            assertValueAt(0) { "foo" == it.value() }

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun queryFlowableDataChangesOfTypeIndicator() {
        val ti = mock<GenericTypeIndicator<List<String>>>()
        val snapshot = dataSnapshotOfTypeIndicator(listOf("foo", "bar"), ti)

        with(TestSubscriber.create<DataValue<List<String>>>()) {
            RxFirebaseDatabase.dataChangesOf(query, ti, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()

            assertThat(values()[0].value())
                    .containsExactly("foo", "bar")

            cancel()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun queryObservableDataChangesOfTypeIndicator() {
        val ti = mock<GenericTypeIndicator<List<String>>>()
        val snapshot = dataSnapshotOfTypeIndicator(listOf("foo", "bar"), ti)

        with(TestObserver.create<DataValue<List<String>>>()) {
            RxFirebaseDatabase.dataChangesOf(query, ti)
                    .subscribe(this)

            // verify addValueEventListener() has called
            query.verifyAddValueEventListenerCalled()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertNoErrors()
            assertNotComplete()

            assertThat(values()[0].value())
                    .containsExactly("foo", "bar")

            dispose()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            // assert no more values
            assertValueCount(1)
        }
    }

    @Test
    fun queryDataOfClazz() {
        val snapshot = dataSnapshotOfClazz("foo")

        with(TestObserver.create<String>()) {
            RxFirebaseDatabase.dataOf(query, String::class.java)
                    .subscribe(this)

            // verify addListenerForSingleValueEvent() has called
            query.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertValue { "foo" == it }

            dispose()
        }
    }

    @Test
    fun queryDataOfGenericTypeIndicator() {
        val ti = mock<GenericTypeIndicator<List<String>>>()
        val snapshot = dataSnapshotOfTypeIndicator(listOf("foo", "bar"), ti)

        with(TestObserver.create<List<String>>()) {
            RxFirebaseDatabase.dataOf(query, ti)
                    .subscribe(this)

            // verify addListenerForSingleValueEvent() has called
            query.verifyAddListenerForSingleValueEvent()

            // simulate the callback
            valueEventListener.lastValue.onDataChange(snapshot)

            assertValueCount(1)
            assertThat(values()[0])
                    .containsExactly("foo", "bar")

            dispose()
        }
    }

    private inline fun DatabaseReference.verifyAddChildEventListenerCalled() {
        verify(this, times(1))
                .addChildEventListener(childEventListener.capture())
    }

    private inline fun DatabaseReference.verifyAddListenerForSingleValueEvent() {
        verify(this, times(1))
                .addListenerForSingleValueEvent(valueEventListener.capture())
    }

    private inline fun DatabaseReference.verifyAddValueEventListenerCalled() {
        verify(this, times(1))
                .addValueEventListener(valueEventListener.capture())
    }

    private inline fun Query.verifyAddChildEventListenerCalled() {
        verify(this, times(1))
                .addChildEventListener(childEventListener.capture())
    }

    private inline fun Query.verifyAddListenerForSingleValueEvent() {
        verify(this, times(1))
                .addListenerForSingleValueEvent(valueEventListener.capture())
    }

    private inline fun Query.verifyAddValueEventListenerCalled() {
        verify(this, times(1))
                .addValueEventListener(valueEventListener.capture())
    }
}
