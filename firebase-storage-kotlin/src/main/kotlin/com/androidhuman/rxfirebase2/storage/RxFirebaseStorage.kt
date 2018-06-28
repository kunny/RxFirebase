@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.androidhuman.rxfirebase2.storage

import android.net.Uri
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Single

inline fun StorageReference.rxPutFile(uri: Uri)
        : Single<UploadTask.TaskSnapshot> = RxFirebaseStorage.putDocument(this, uri)

inline fun StorageReference.rxPutFile(uri: Uri, metadata: StorageMetadata)
        : Single<UploadTask.TaskSnapshot> = RxFirebaseStorage.putDocument(this, uri, metadata)

inline fun StorageReference.rxDownloadUrl()
        : Single<Uri> = RxFirebaseStorage.downloadUrl(this)

