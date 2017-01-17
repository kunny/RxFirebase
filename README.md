# RxFirebase
[![CircleCI](https://circleci.com/gh/kunny/RxFirebase.svg?style=shield)](https://circleci.com/gh/kunny/RxFirebase)
[![Coverage Status](https://coveralls.io/repos/github/kunny/RxFirebase/badge.svg?branch=master)](https://coveralls.io/github/kunny/RxFirebase?branch=master)
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase/common/badge.svg)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxFirebase-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4496)

RxJava binding APIs for [Firebase](https://firebase.google.com/) Android SDK.

## Modules

### firebase-auth

RxJava binding APIs for [Firebase Authentication](https://firebase.google.com/docs/auth/).

```groovy
compile ('com.androidhuman.rxfirebase:firebase-auth:9.80.0') {
    transitive = false
}
```

### firebase-auth-kotlin

Kotlin support module for `firebase-auth`.

```groovy
compile ('com.androidhuman.rxfirebase:firebase-auth-kotlin:9.80.0') {
    transitive = false
}
```

### firebase-database

RxJava binding APIs for [Firebase Realtime Database](https://firebase.google.com/docs/database/) Android SDK.

```groovy
compile ('com.androidhuman.rxfirebase:firebase-database:9.80.0') {
    transitive = false
}
```

### firebase-database-kotlin

Kotlin support module for `firebase-database`

```groovy
compile ('com.androidhuman.rxfirebase:firebase-database-kotlin:9.80.0') {
    transitive = false
}
```

Each kotlin support module maps all methods in Java module into an extension function on following classes:

- firebase-auth-kotlin
  - `FirebaseAuth`
  - `FirebaseUser`
- firebase-database-kotlin
  - `DatabaseReference`

Basically, extension function has same name of methods in `RxXXX` classes in Java module.

If extension function conflicts with a method in a class that is being extended, it will be renamed with `rx` prefix.

For more details, please refer to following 'Usage' section.

## Usage

Here are some usages of `RxFirebase`. Since it provides just a wrapper for Firebase Android SDK, see [official documentation](https://firebase.google.com/docs/) for the details.

### Firebase Authentication

#### Get the currently signed-in user (Listener)

Get currently signed-in user by `Firebasebase.AuthStateChangeListener`.

As a listener, it will emit `FirebaseAuth` object on each auth state changes until unsubscribed.

Note that `RxFirebaseAuth.authStateChanges()` will emit initial value on subscribe.

Java:
```java
RxFirebaseAuth.authStateChanges(FirebaseAuth.getInstance())
        .subscribe(new Action1<FirebaseAuth>() {
            @Override
            public void call(FirebaseAuth firebaseAuth) {
                // Do something when auth state changes.
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                // Handle error
            }
        });
```

Kotlin:
```kotlin
FirebaseAuth.getInstance().authStateChanges()
        .subscribe({
            // Do something when auth state changes.
        }, {
            // Handle error
        })
```

#### Get the currently signed-in user (getCurrentUser())

Since `FirebaseAuth.getCurrentUser()` might return null when auth object has not finished initializing, it returns `Optional` wrapper of `FirebaseUser` object to prevent null pointer exception.

Java:
```java
RxFirebaseAuth.getCurrentUser(FirebaseAuth.getInstance())
        .subscribe(new Action1<Optional<FirebaseUser>>() {
            @Override
            public void call(Optional<FirebaseUser> user) {
                if (user.isPresent()) {
                    // Do something with user
                } else {
                    // There is not signed in user or
                    // Firebase instance was not fully initialized.
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                // Handle error
            }
        });
```


Kotlin:
```kotlin
FirebaseAuth.getInstance().rxGetCurrentUser()
        .subscribe({
            if (user.isPresent) {
                // Do something with user
            } else {
                // There is not signed in user or
                // Firebase instance was not fully initialized.
            }
        }, {
            // Handle error
        })
```

#### Sign in anonymously

Uses [Anonymous Authentication](https://firebase.google.com/docs/auth/android/anonymous-auth) for sign in. Note that it emits `FirebaseUser` object of currently signed-in user.

Java:
```java
RxFirebaseAuth.signInAnonymous(FirebaseAuth.getInstance())
        .subscribe(new Action1<FirebaseUser>() {
            @Override
            public void call(FirebaseUser user) {
                // Do something with anonymous user
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                // Handle error
            }
        });
```

Kotlin:
```kotlin
FirebaseAuth.getInstance().rxSignInAnonymous()
        .subscribe({
            // Do something with anonymous user
        }, {
            // Handle error
        })
```

#### Update a user's profile

For a method which returns `Task<Void>` as a result, it is converted as `TaskResult`.

Java:
```java
FirebaseUser user = ...;

UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
        .setDisplayName("John Doe")
        .setPhotoUri(Uri.parse("http://my.photo/johndoe"))
        .build();

RxFirebaseUser.updateProfile(user, request)
        .subscribe(new Action1<TaskResult>() {
            @Override
            public void call(TaskResult result) {
                if (result.isSuccess()) {
                    // Update successful
                } else {
                    // Update was not successful
                }
            }
        });
```

Kotlin:
```kotlin
val user: FirebaseUser = ...

val request = UserProfileChangeRequest.Builder()
        .setDisplayName("John Doe")
        .setPhotoUri(Uri.parse("http://my.photo/johndoe"))
        .build()

user.rxUpdateProfile(request)
        .subscribe {
            if (it.isSuccess) {
                // Update successful
            } else {
                // Update was not successful
            }
        }
```

### Firebase Realtime Database

#### Write to your database

Retrieve an instance of your database using `FirebaseDatabase.getInstance()` and pass the reference of the location to `RxFirebase.setValue()` with a value.

Java:

```java
DatabaseReference ref = ...;

RxFirebaseDatabase.setValue(ref, "Lorem ipsum")
        .subscribe(new Action1<TaskResult>() {
            @Override
            public void call(TaskResult taskResult) {
                if (taskResult.isSuccess()) {
                    // Update successful
                } else {
                    // Something went wrong
                }
            }
        });
```

Kotlin:

```kotlin
val ref: DatabaseReference = ...;
ref.rxSetValue("Lorem ipsum")
        .subscribe {
            if (it.isSuccess) {
                // Update successful
            } else {
                // Somthing went wrong
            }
        }
```

#### Update specific fields

To simultaneously write to specific children of a note without overwriting other child nodes, use the `RxFirebase.updateChildren()` method.

Java:

```java
DatabaseReference ref = ...;

Map<String, Object> update = new HashMap<>();
update.put("/posts/foo", /* Post values */);
update.put("/user-posts/bar", /* Post values */);

RxFirebaseDatabase.updateChildren(ref, update)
        .subscribe(new Action1<TaskResult>() {
            @Override
            public void call(TaskResult taskResult) {
                // Do something with result
            }
        });
```
Kotlin:

```kotlin
val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()

val update = mapOf(
              "/posts/foo" to /* Post values */,
              "user-posts/bar" to /* Post values */)

ref.rxUpdateChildren(update)
        .subscribe {
            // Do something with result
        }
```

### Read from your database

#### Listen for value events

You can use the `RxFirebase.dataChanges()` method to get a snapshot(`DataSnapshot`) of the contents at a given path, as they existed at the time of the event.

This method will emit an event once when subscribed, and again every time the data, including children, changes.

Java:

```java
DatabaseReference ref = ...;

RxFirebaseDatabase.dataChanges(ref)
        .subscribe(new Action1<DataSnapshot>() {
            @Override
            public void call(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Do something with data
                } else {
                    // Data does not exists
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                // Handle error
            }
        });
```

Kotlin:

```kotlin
val ref: DatabaseReference = ...

ref.dataChanges()
        .subscribe({
            if (it.exists()) {
                // Do something with data
            } else {
                // Data does not exists
            }
        }) {
            // Handle error
        }
```

If you want to get a data as a native object, you can use `RxFirebaseDatabase.dataChangesOf(Class<T>)` or `RxFirebaseDatabase.dataChangesOf(GenericTypeIndicator<T>)`.

You *must* unsubscribe a subscription once you're done with listening value events to prevent memory leak.

#### Listen for child events

Child events are trigger in response to specific operations that happen to the children of a node from an operation such as a new child added through the `push()` method or a child being update through the `updateChildren()` method.

You can listen for child events by `RxFirebaseDatabase.childEvents()` method, which emits an event as following:

- `ChildAddEvent` - Emitted on `ChildEventListener.onChildAdded()` call
- `ChildChangeEvent` - Emitted on `ChildEventListener.onChildChanged()` call
- `ChildMoveEvent` - Emitted on `ChildEventListener.onChildMoved()` call
- `ChildRemoveEvent` - Emitted on `ChildEventListener.onChildRemoved()` call

`RxFirebaseDatabase.childEvents()` will emit all types of event by default. If you need to listen for specific event, you can filter by using `ofType()` operator in `RxJava` as following:

Java:

```java
DatabaseReference ref = ...;

RxFirebaseDatabase.childEvents(ref)
        .ofType(ChildAddEvent.class)
        .subscribe(new Action1<ChildAddEvent>() {
            @Override
            public void call(ChildAddEvent childAddEvent) {
                // Handle for Child add event
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                // Handle error
            }
        });
```

Kotlin:

```kotlin
val ref: DatabaseReference = ...

ref.childEvents()
        .ofType(ChildAddEvent::class.java)
        .subscribe({
            // Handle for child add event
        }) {
            // Handle error
        }
```

You *must* unsubscribe a subscription once you're done with listening child events to prevent memory leak.

#### Read data once

This is useful for data that only needs to be loaded once and isn't expected to change frequently or require active listening.

Similar to listening for the data changes, you can use `RxFirebaseDatabase.data()` method to get an `Optional` wrapper of static snapshot of the contents.

Java:

```java
DatabaseReference ref = ...;

RxFirebaseDatabase.data(ref)
        .subscribe(new Action1<DataSnapshot>() {
            @Override
            public void call(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Do something with data
                } else {
                    // Data does not exists
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                // Handle error
            }
        });
```

Kotlin:

```kotlin
val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()

ref.data()
        .subscribe({
            if (it.exists()) {
                // Do something with data
            } else {
                // Data does not exists
            }
        }) {
            // Handle error
        }
```

## Versioning

RxFirebase uses a versioning rule that is related to corresponding Firebase's version by following rule:

```
RxFirebaseVersion : {major}.{minor}.{patch} =
    {Firebase major}.{Firebase minor * 10 + Firebase patch}.{RxFirebase patch}
```

For example, a library version that depends on `9.6.0` version of Firebase SDK, whose patch version is `1` will be `9.60.1`.

## Development Snapshot

Snapshots of the development version are available in [Sonatype's `snapshots` repository](Sonatype's snapshots repository).

You can register snapshots repo as your project's remote repo as following:

```groovy
repositories {
    ... other remote repositories ...

    // Add following line
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

Currently, there is no snapshot available.

## License

```
Copyright 2016 Taeho Kim <jyte82@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
