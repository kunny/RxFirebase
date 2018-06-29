package com.androidhuman.rxfirebase2.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
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
    lateinit var query: Query

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun addCollectionReference() {

    }

    @Test
    fun addCollectionReferenceMap() {

    }

    @Test
    fun dataDocumentReference() {

    }

    @Test
    fun dataDocumentReferenceSource() {

    }

    @Test
    fun dataQuery() {

    }

    @Test
    fun dataQuerySource() {

    }

    @Test
    fun dataChangesDocumentReferenceFlowable() {

    }

    @Test
    fun dataChangesDocumentReference() {

    }

    @Test
    fun dataChangesQueryFlowable() {

    }

    @Test
    fun dataChangesQuery() {

    }

    @Test
    fun delete() {

    }

    @Test
    fun set() {

    }

    @Test
    fun setOptions() {

    }

    @Test
    fun setMap() {

    }

    @Test
    fun setMapOptions() {

    }

    @Test
    fun update() {

    }

}