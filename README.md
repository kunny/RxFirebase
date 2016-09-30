# RxFirebase
[![CircleCI](https://circleci.com/gh/kunny/RxFirebase.svg?style=shield)](https://circleci.com/gh/kunny/RxFirebase)
[![Coverage Status](https://coveralls.io/repos/github/kunny/RxFirebase/badge.svg?branch=master)](https://coveralls.io/github/kunny/RxFirebase?branch=master)

RxJava binding APIs for [Firebase](https://firebase.google.com/) Android SDK.

## Modules

### firebase-auth

RxJava binding APIs for [Firebase Authentication](https://firebase.google.com/docs/auth/).

```groovy
compile 'com.androidhuman.rxfirebase:firebase-auth:{version}'
```

### firebase-auth-kotlin

Kotlin support module for `firebase-auth`.

```groovy
compile 'com.androidhuman.rxfirebase:firebase-auth-kotlin:{version}'
```

### firebase-database

RxJava binding APIs for [Firebase Realtime Database](https://firebase.google.com/docs/database/) Android SDK.

```groovy
compile 'com.androidhuman.rxfirebase:firebase-database:{version}'
```

### firebase-database-kotlin

```groovy
compile 'com.androidhuman.rxfirebase:firebase-database-kotlin:{version}'
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
In Kotlin support module, it converts `Optional` into its native nullable reference, `FirebaseUser?`.

Kotlin:
```kotlin
FirebaseAuth.getInstance().rxGetCurrentUser()
        .subscribe({
            if (null != it) {
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

TBD

## Versioning

RxFirebase uses a versioning rule that is related to corresponding Firebase's version by following rule:

```
RxFirebaseVersion : {major}.{minor}.{patch} = {Firebase major}.{Firebase minor * 10 + Firebase patch}.{RxFirebase patch}
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
