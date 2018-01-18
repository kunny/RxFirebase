package com.androidhuman.rxfirebase2.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.GenericTypeIndicator
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever

internal fun databaseError(): DatabaseError {
    return mock<DatabaseError>().apply {
        whenever(toException())
                .thenReturn(mock<DatabaseException>())
    }
}

internal fun <T> dataSnapshot(value: T): DataSnapshot {
    return mock<DataSnapshot>().apply {
        whenever(exists())
                .thenReturn(true)

        whenever(getValue())
                .thenReturn(value)
    }
}

internal inline fun <reified T> dataSnapshotOfClazz(value: T): DataSnapshot {
    return mock<DataSnapshot>().apply {
        whenever(exists())
                .thenReturn(true)

        whenever(getValue(T::class.java))
                .thenReturn(value)
    }
}

internal fun <T> dataSnapshotOfTypeIndicator(
        value: T, typeIndicator: GenericTypeIndicator<T>): DataSnapshot {
    return mock<DataSnapshot>().apply {
        whenever(exists())
                .thenReturn(true)

        whenever(getValue(typeIndicator))
                .thenReturn(value)
    }
}
