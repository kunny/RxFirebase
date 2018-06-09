package com.androidhuman.rxfirebase2.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ProviderQueryResult
import com.google.firebase.auth.SignInMethodQueryResult
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever

internal fun succeedAuthResultTask(_email: String? = null): Task<AuthResult> {
    return mock<Task<AuthResult>>().apply {
        whenever(isSuccessful)
                .thenReturn(true)

        val authResult = mock<AuthResult>().apply {
            val firebaseUser = mock<FirebaseUser>().apply {
                whenever(email)
                        .thenReturn(_email)

                whenever(isAnonymous)
                        .thenReturn(null == _email)
            }

            whenever(user)
                    .thenReturn(firebaseUser)
        }

        whenever(result)
                .thenReturn(authResult)
    }
}

internal fun succeedGetTokenResultTask(_token: String): Task<GetTokenResult> {
    return mock<Task<GetTokenResult>>().apply {
        whenever(isSuccessful)
                .thenReturn(true)

        val tokenResult = mock<GetTokenResult>().apply {
            whenever(token)
                    .thenReturn(_token)
        }

        whenever(result)
                .thenReturn(tokenResult)
    }
}

internal fun succeedProvidersResultTask(
        _providers: List<String>?): Task<ProviderQueryResult> {
    return mock<Task<ProviderQueryResult>>().apply {
        whenever(isSuccessful)
                .thenReturn(true)

        val providerQueryResult = mock<ProviderQueryResult>().apply {
            whenever(providers)
                    .thenReturn(_providers)
        }

        whenever(result)
                .thenReturn(providerQueryResult)
    }
}

internal fun succeedSignInMethodsResultTask(
        _methods: List<String>?): Task<SignInMethodQueryResult> {
    return mock<Task<SignInMethodQueryResult>>().apply {
        whenever(isSuccessful)
                .thenReturn(true)

        val signInMethodQueryResult = mock<SignInMethodQueryResult>().apply {
            whenever(signInMethods)
                    .thenReturn(_methods)
        }

        whenever(result)
                .thenReturn(signInMethodQueryResult)
    }
}

internal fun succeedVoidTask(): Task<Void> {
    return mock<Task<Void>>().apply {
        whenever(isSuccessful)
                .thenReturn(true)
    }
}

internal fun <T> failedTask(e: Exception): Task<T> {
    return mock<Task<T>>().apply {
        whenever(isSuccessful)
                .thenReturn(false)

        whenever(exception)
                .thenReturn(e)
    }
}
