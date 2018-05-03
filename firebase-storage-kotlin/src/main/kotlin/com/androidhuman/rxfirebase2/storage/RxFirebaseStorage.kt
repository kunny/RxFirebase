@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package com.androidhuman.rxfirebase2.storage

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Single

inline fun StorageReference.rxPutFIle(uri: Uri)
        : Single<UploadTask.TaskSnapshot> = RxFirebaseStorage.putDocument(this, uri)
