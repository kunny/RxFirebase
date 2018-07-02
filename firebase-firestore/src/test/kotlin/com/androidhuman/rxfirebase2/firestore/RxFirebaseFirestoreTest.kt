package com.androidhuman.rxfirebase2.firestore

import com.androidhuman.rxfirebase2.firestore.model.Empty
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RxFirebaseFirestoreTest {

    @Mock
    lateinit var collection: CollectionReference

    @Mock
    lateinit var document: DocumentReference

    @Mock
    lateinit var docSnapshot: DocumentSnapshot

    @Mock
    lateinit var querySnapshot: QuerySnapshot

    @Mock
    lateinit var query: Query

    private val docRefCompleteListener = argumentCaptor<OnCompleteListener<DocumentReference>>()

    private val docSnapshotCompleteListener = argumentCaptor<OnCompleteListener<DocumentSnapshot>>()

    private val querySnapshotCompleteListener = argumentCaptor<OnCompleteListener<QuerySnapshot>>()

    private val voidCompleteListener = argumentCaptor<OnCompleteListener<Void>>()

    private val docSnapshotEventListener = argumentCaptor<EventListener<DocumentSnapshot>>()

    private val querySnapshotEventListener = argumentCaptor<EventListener<QuerySnapshot>>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun addCollectionReference() {
        with(TestObserver.create<DocumentReference>()) {
            // mock Task
            val task = mock<Task<DocumentReference>>().apply {
                whenever(result)
                        .thenReturn(document)
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            // mock CollectionReference.add()
            whenever(collection.add("foo"))
                    .thenReturn(task)

            // mock CollectionReference.add().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.add(collection, "foo")
                    .subscribe(this)

            // verify add() has called
            verify(collection, times(1))
                    .add("foo")

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docRefCompleteListener.capture())

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            assertValue { it == document }

            dispose()

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            // assert no more values are emitted
            assertEquals(1, valueCount())
        }
    }

    @Test
    fun addCollectionReferenceFailed() {
        with(TestObserver.create<DocumentReference>()) {
            // mock Task
            val task = mock<Task<DocumentReference>>().apply {
                whenever(exception)
                        .thenReturn(IllegalStateException())
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // mock CollectionReference.add()
            whenever(collection.add("foo"))
                    .thenReturn(task)

            // mock CollectionReference.add().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.add(collection, "foo")
                    .subscribe(this)

            // verify add() has called
            verify(collection, times(1))
                    .add("foo")

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docRefCompleteListener.capture())

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun addCollectionReferenceFailedWithUnknown() {
        with(TestObserver.create<DocumentReference>()) {
            // mock Task
            val task = mock<Task<DocumentReference>>().apply {
                whenever(exception)
                        .thenReturn(null)
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // mock CollectionReference.add()
            whenever(collection.add("foo"))
                    .thenReturn(task)

            // mock CollectionReference.add().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.add(collection, "foo")
                    .subscribe(this)

            // verify add() has called
            verify(collection, times(1))
                    .add("foo")

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docRefCompleteListener.capture())

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun addCollectionReferenceMap() {
        with(TestObserver.create<DocumentReference>()) {
            // mock Task
            val task = mock<Task<DocumentReference>>().apply {
                whenever(result)
                        .thenReturn(document)
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            // mock value
            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock CollectionReference.add()
            whenever(collection.add(data))
                    .thenReturn(task)

            // mock CollectionReference.add().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.add(collection, data)
                    .subscribe(this)

            // verify add() has called
            verify(collection, times(1))
                    .add(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docRefCompleteListener.capture())

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            assertValue { it == document }

            dispose()

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            // assert no more values are emitted
            assertEquals(1, valueCount())
        }
    }

    @Test
    fun addCollectionReferenceMapFailed() {
        with(TestObserver.create<DocumentReference>()) {
            // mock Task
            val task = mock<Task<DocumentReference>>().apply {
                whenever(exception)
                        .thenReturn(IllegalStateException())
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // mock value
            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock CollectionReference.add()
            whenever(collection.add(data))
                    .thenReturn(task)

            // mock CollectionReference.add().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.add(collection, data)
                    .subscribe(this)

            // verify add() has called
            verify(collection, times(1))
                    .add(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docRefCompleteListener.capture())

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun addCollectionReferenceMapFailedWithUnknown() {
        with(TestObserver.create<DocumentReference>()) {
            // mock Task
            val task = mock<Task<DocumentReference>>().apply {
                whenever(exception)
                        .thenReturn(null)
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // mock value
            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock CollectionReference.add()
            whenever(collection.add(data))
                    .thenReturn(task)

            // mock CollectionReference.add().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.add(collection, data)
                    .subscribe(this)

            // verify add() has called
            verify(collection, times(1))
                    .add(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docRefCompleteListener.capture())

            // simulate callback
            docRefCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun dataDocumentReference() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(true)

            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(docSnapshot)
            }

            // mock DocumentReference.get()
            whenever(document.get())
                    .thenReturn(task)

            // mock DocumentReference.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document)
                    .subscribe(this)

            // verify get() has called
            verify(document, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it.value() == docSnapshot }

            dispose()

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun dataDocumentReferenceEmptyValue() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(false)

            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(docSnapshot)
            }

            // mock DocumentReference.get()
            whenever(document.get())
                    .thenReturn(task)

            // mock DocumentReference.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document)
                    .subscribe(this)

            // verify get() has called
            verify(document, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataDocumentReferenceFailed() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            // mock DocumentReference.get()
            whenever(document.get())
                    .thenReturn(task)

            // mock DocumentReference.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document)
                    .subscribe(this)

            // verify get() has called
            verify(document, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun dataDocumentReferenceFailedWithUnknown() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            // mock DocumentReference.get()
            whenever(document.get())
                    .thenReturn(task)

            // mock DocumentReference.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document)
                    .subscribe(this)

            // verify get() has called
            verify(document, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun dataDocumentReferenceSource() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(true)

            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(docSnapshot)
            }

            // mock DocumentReference.get(source)
            whenever(document.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock DocumentReference.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(document, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it.value() == docSnapshot }

            dispose()

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun dataDocumentReferenceSourceEmptyValue() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(false)

            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(docSnapshot)
            }

            // mock DocumentReference.get(source)
            whenever(document.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock DocumentReference.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(document, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataDocumentReferenceSourceFailed() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            // mock DocumentReference.get(source)
            whenever(document.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock DocumentReference.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(document, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun dataDocumentReferenceSourceFailedWithUnknown() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock Task
            val task = mock<Task<DocumentSnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            // mock DocumentReference.get(source)
            whenever(document.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock DocumentReference.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(document, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(document, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(docSnapshotCompleteListener.capture())

            // simulate callback
            docSnapshotCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun dataQuery() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(false)

            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(querySnapshot)
            }

            // mock Query.get()
            whenever(query.get())
                    .thenReturn(task)

            // mock Query.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it.value() == querySnapshot }

            dispose()

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun dataQueryEmptyValue() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(true)

            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(querySnapshot)
            }

            // mock Query.get()
            whenever(query.get())
                    .thenReturn(task)

            // mock Query.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataQueryFailed() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            // mock Query.get()
            whenever(query.get())
                    .thenReturn(task)

            // mock Query.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun dataQueryFailedWithUnknown() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            // mock Query.get()
            whenever(query.get())
                    .thenReturn(task)

            // mock Query.get().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun dataQuerySource() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(false)

            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(querySnapshot)
            }

            // mock Query.get(source)
            whenever(query.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock Query.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it.value() == querySnapshot }

            dispose()
        }
    }

    @Test
    fun dataQuerySourceEmptyValue() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(true)

            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
                whenever(result)
                        .thenReturn(querySnapshot)
            }

            // mock Query.get(source)
            whenever(query.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock Query.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataQuerySourceFailed() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            // mock Query.get(source)
            whenever(query.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock Query.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun dataQuerySourceFailedWithUnknown() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock Task
            val task = mock<Task<QuerySnapshot>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            // mock Query.get(source)
            whenever(query.get(Source.DEFAULT))
                    .thenReturn(task)

            // mock Query.get(source).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.data(query, Source.DEFAULT)
                    .subscribe(this)

            // verify get(source) has called
            verify(query, times(1))
                    .get(Source.DEFAULT)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(querySnapshotCompleteListener.capture())

            // simulate callback
            querySnapshotCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun dataChangesDocumentReferenceFlowable() {
        with(TestSubscriber.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(true)

            // mock DocumentReference
            whenever(document.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(document, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(document, times(1))
                    .addSnapshotListener(docSnapshotEventListener.capture())

            // simulate callback
            docSnapshotEventListener.lastValue.onEvent(docSnapshot, null)

            assertValue { it.value() == docSnapshot }

            dispose()

            // simulate callback
            docSnapshotEventListener.lastValue.onEvent(docSnapshot, null)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun dataChangesDocumentReferenceFlowableEmptyValue() {
        with(TestSubscriber.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(false)

            // mock DocumentReference
            whenever(document.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(document, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(document, times(1))
                    .addSnapshotListener(docSnapshotEventListener.capture())

            // simulate callback
            docSnapshotEventListener.lastValue.onEvent(docSnapshot, null)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataChangesDocumentReferenceFlowableFailed() {
        with(TestSubscriber.create<Value<DocumentSnapshot>>()) {
            // mock DocumentReference
            whenever(document.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(document, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(document, times(1))
                    .addSnapshotListener(docSnapshotEventListener.capture())

            // simulate callback
            val exception = mock<FirebaseFirestoreException>()

            docSnapshotEventListener.lastValue.onEvent(docSnapshot, exception)

            assertError(exception)

            dispose()
        }
    }

    @Test
    fun dataChangesDocumentReference() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(true)

            // mock DocumentReference
            whenever(document.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(document)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(document, times(1))
                    .addSnapshotListener(docSnapshotEventListener.capture())

            // simulate callback
            docSnapshotEventListener.lastValue.onEvent(docSnapshot, null)

            assertValue { it.value() == docSnapshot }

            dispose()
        }
    }

    @Test
    fun dataChangesDocumentReferenceEmptyValue() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock DocumentSnapshot
            whenever(docSnapshot.exists())
                    .thenReturn(false)

            // mock DocumentReference
            whenever(document.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(document)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(document, times(1))
                    .addSnapshotListener(docSnapshotEventListener.capture())

            // simulate callback
            docSnapshotEventListener.lastValue.onEvent(docSnapshot, null)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataChangesDocumentReferenceFailed() {
        with(TestObserver.create<Value<DocumentSnapshot>>()) {
            // mock DocumentReference
            whenever(document.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(document)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(document, times(1))
                    .addSnapshotListener(docSnapshotEventListener.capture())

            // simulate callback
            val exception = mock<FirebaseFirestoreException>()

            docSnapshotEventListener.lastValue.onEvent(docSnapshot, exception)

            assertError(exception)

            dispose()
        }
    }

    @Test
    fun dataChangesQueryFlowable() {
        with(TestSubscriber.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(false)

            // mock Query
            whenever(query.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(query, times(1))
                    .addSnapshotListener(querySnapshotEventListener.capture())

            // simulate callback
            querySnapshotEventListener.lastValue.onEvent(querySnapshot, null)

            assertValue { it.value() == querySnapshot }

            dispose()

            // simulate callback
            querySnapshotEventListener.lastValue.onEvent(querySnapshot, null)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun dataChangesQueryFlowableEmptyValue() {
        with(TestSubscriber.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(true)

            // mock Query
            whenever(query.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(query, times(1))
                    .addSnapshotListener(querySnapshotEventListener.capture())

            // simulate callback
            querySnapshotEventListener.lastValue.onEvent(querySnapshot, null)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataChangesQueryFlowableFailed() {
        with(TestSubscriber.create<Value<QuerySnapshot>>()) {
            // mock Query
            whenever(query.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(query, BackpressureStrategy.BUFFER)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(query, times(1))
                    .addSnapshotListener(querySnapshotEventListener.capture())

            // simulate callback
            val exception = mock<FirebaseFirestoreException>()

            querySnapshotEventListener.lastValue.onEvent(querySnapshot, exception)

            assertError(exception)

            dispose()
        }
    }

    @Test
    fun dataChangesQuery() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(false)

            // mock Query
            whenever(query.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(query)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(query, times(1))
                    .addSnapshotListener(querySnapshotEventListener.capture())

            // simulate callback
            querySnapshotEventListener.lastValue.onEvent(querySnapshot, null)

            assertValue { it.value() == querySnapshot }

            dispose()

            // simulate callback
            querySnapshotEventListener.lastValue.onEvent(querySnapshot, null)

            // assert no more values are emitted
            assertValueCount(1)
        }
    }

    @Test
    fun dataChangesQueryEmptyValue() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock QuerySnapshot
            whenever(querySnapshot.isEmpty)
                    .thenReturn(true)

            // mock Query
            whenever(query.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(query)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(query, times(1))
                    .addSnapshotListener(querySnapshotEventListener.capture())

            // simulate callback
            querySnapshotEventListener.lastValue.onEvent(querySnapshot, null)

            assertValue { it is Empty }

            dispose()
        }
    }

    @Test
    fun dataChangesQueryFailed() {
        with(TestObserver.create<Value<QuerySnapshot>>()) {
            // mock Query
            whenever(query.addSnapshotListener(any()))
                    .thenReturn(mock())

            // call a method being tested
            RxFirebaseFirestore.dataChanges(query)
                    .subscribe(this)

            // verify addSnapshotListener has called
            verify(query, times(1))
                    .addSnapshotListener(querySnapshotEventListener.capture())

            // simulate callback
            val exception = mock<FirebaseFirestoreException>()

            querySnapshotEventListener.lastValue.onEvent(querySnapshot, exception)

            assertError(exception)

            dispose()
        }
    }

    @Test
    fun delete() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            // mock DocumentReference.delete()
            whenever(document.delete())
                    .thenReturn(task)

            // mock DocumentReference.delete().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.delete(document)
                    .subscribe(this)

            // verify delete() has called
            verify(document, times(1))
                    .delete()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertComplete()

            dispose()

            // mock failedTask
            val failedTask = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // simulate callback
            voidCompleteListener.lastValue.onComplete(failedTask)

            // assert no more values are emitted
            assertNoErrors()
        }
    }

    @Test
    fun deleteFailed() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            // mock DocumentReference.delete()
            whenever(document.delete())
                    .thenReturn(task)

            // mock DocumentReference.delete().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.delete(document)
                    .subscribe(this)

            // verify delete() has called
            verify(document, times(1))
                    .delete()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun deleteFailedWithUnknown() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            // mock DocumentReference.delete()
            whenever(document.delete())
                    .thenReturn(task)

            // mock DocumentReference.delete().addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.delete(document)
                    .subscribe(this)

            // verify delete() has called
            verify(document, times(1))
                    .delete()

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun set() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            // mock DocumentReference.set(value)
            whenever(document.set("test"))
                    .thenReturn(task)

            // mock DocumentReference.set(value).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, "test")
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set("test")

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertComplete()

            dispose()

            // mock failedTask
            val failedTask = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // simulate callback
            voidCompleteListener.lastValue.onComplete(failedTask)

            // assert no more values are emitted
            assertNoErrors()
        }
    }

    @Test
    fun setFailed() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            // mock DocumentReference.set(value)
            whenever(document.set("test"))
                    .thenReturn(task)

            // mock DocumentReference.set(value).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, "test")
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set("test")

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun setFailedWithUnknown() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            // mock DocumentReference.set(value)
            whenever(document.set("test"))
                    .thenReturn(task)

            // mock DocumentReference.set(value).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, "test")
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set("test")

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun setOptions() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            // mock DocumentReference.set(value, options)
            whenever(document.set("test", SetOptions.merge()))
                    .thenReturn(task)

            // mock DocumentReference.set(value, options).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, "test", SetOptions.merge())
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set("test", SetOptions.merge())

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertComplete()

            dispose()
        }
    }

    @Test
    fun setOptionsFailed() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            // mock DocumentReference.set(value, options)
            whenever(document.set("test", SetOptions.merge()))
                    .thenReturn(task)

            // mock DocumentReference.set(value, options).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, "test", SetOptions.merge())
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set("test", SetOptions.merge())

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun setOptionsFailedWithUnknown() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            // mock DocumentReference.set(value, options)
            whenever(document.set("test", SetOptions.merge()))
                    .thenReturn(task)

            // mock DocumentReference.set(value, options).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, "test", SetOptions.merge())
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set("test", SetOptions.merge())

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun setMap() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.set(map)
            whenever(document.set(data))
                    .thenReturn(task)

            // mock DocumentReference.set(map).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, data)
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertComplete()

            dispose()

            // mock failedTask
            val failedTask = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // simulate callback
            voidCompleteListener.lastValue.onComplete(failedTask)

            // assert no errors are emitted
            assertNoErrors()
        }
    }

    @Test
    fun setMapFailed() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.set(map)
            whenever(document.set(data))
                    .thenReturn(task)

            // mock DocumentReference.set(map).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, data)
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun setMapFailedWithUnknown() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.set(map)
            whenever(document.set(data))
                    .thenReturn(task)

            // mock DocumentReference.set(map).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, data)
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .set(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun setMapOptions() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.set(map, options)
            whenever(document.set(data, SetOptions.merge()))
                    .thenReturn(task)

            // mock DocumentReference.set(map, options).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, data, SetOptions.merge())
                    .subscribe(this)

            // verify set(value, options) has called
            verify(document, times(1))
                    .set(data, SetOptions.merge())

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertComplete()

            dispose()
        }
    }

    @Test
    fun setMapOptionsFailed() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.set(map, options)
            whenever(document.set(data, SetOptions.merge()))
                    .thenReturn(task)

            // mock DocumentReference.set(map, options).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, data, SetOptions.merge())
                    .subscribe(this)

            // verify set(value, options) has called
            verify(document, times(1))
                    .set(data, SetOptions.merge())

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun setMapOptionsFailedWithUnknown() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.set(map, options)
            whenever(document.set(data, SetOptions.merge()))
                    .thenReturn(task)

            // mock DocumentReference.set(map, options).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.set(document, data, SetOptions.merge())
                    .subscribe(this)

            // verify set(value, options) has called
            verify(document, times(1))
                    .set(data, SetOptions.merge())

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

    @Test
    fun update() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(true)
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.update(map)
            whenever(document.update(data))
                    .thenReturn(task)

            // mock DocumentReference.update(map).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.update(document, data)
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .update(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertComplete()

            dispose()

            // mock failedTask
            val failedTask = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
            }

            // simulate callback
            voidCompleteListener.lastValue.onComplete(failedTask)

            // assert no errors are emitted
            assertNoErrors()
        }
    }

    @Test
    fun updateFailed() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(IllegalStateException())
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.update(map)
            whenever(document.update(data))
                    .thenReturn(task)

            // mock DocumentReference.update(map).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.update(document, data)
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .update(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(IllegalStateException::class.java)

            dispose()
        }
    }

    @Test
    fun updateFailedWithUnknown() {
        with(TestObserver.create<Any>()) {
            // mock Task
            val task = mock<Task<Void>>().apply {
                whenever(isSuccessful)
                        .thenReturn(false)
                whenever(exception)
                        .thenReturn(null)
            }

            val data = mapOf("Foo" to "foo", "Bar" to "bar")

            // mock DocumentReference.update(map)
            whenever(document.update(data))
                    .thenReturn(task)

            // mock DocumentReference.update(map).addOnCompleteListener()
            whenever(task.addOnCompleteListener(any()))
                    .thenReturn(task)

            // call a method being tested
            RxFirebaseFirestore.update(document, data)
                    .subscribe(this)

            // verify set(value) has called
            verify(document, times(1))
                    .update(data)

            // verify addOnCompleteListener() has called
            verify(task, times(1))
                    .addOnCompleteListener(voidCompleteListener.capture())

            // simulate callback
            voidCompleteListener.lastValue.onComplete(task)

            assertError(UnknownError::class.java)

            dispose()
        }
    }

}