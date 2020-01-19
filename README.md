# RxFirebase
[![CircleCI](https://circleci.com/gh/kunny/RxFirebase.svg?style=shield)](https://circleci.com/gh/kunny/RxFirebase)
[![codecov](https://codecov.io/gh/kunny/RxFirebase/branch/master/graph/badge.svg)](https://codecov.io/gh/kunny/RxFirebase)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxFirebase-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4496)

RxJava binding APIs for [Firebase](https://firebase.google.com/) Android SDK.

## Modules

### firebase-auth
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-auth/badge.svg)

RxJava binding APIs for [Firebase Authentication](https://firebase.google.com/docs/auth/) Android SDK.

```groovy
// firebase-auth
implementation 'com.androidhuman.rxfirebase2:firebase-auth:19.2.0.0'
implementation 'com.google.firebase:firebase-auth:19.2.0'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2'

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```

### firebase-auth-kotlin
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-auth-kotlin/badge.svg)

Kotlin support module for `firebase-auth`.

```groovy
// firebase-auth
implementation 'com.androidhuman.rxfirebase2:firebase-auth-kotlin:19.2.0.0'
implementation 'com.androidhuman.rxfirebase2:firebase-auth:19.2.0.0'
implementation 'com.google.firebase:firebase-auth:19.2.0'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2'

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```

### firebase-database
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-database/badge.svg)

RxJava binding APIs for [Firebase Realtime Database](https://firebase.google.com/docs/database/) Android SDK.

```groovy
// firebase-database
implementation 'com.androidhuman.rxfirebase2:firebase-database:16.0.3.0'
implementation 'com.google.firebase:firebase-database:16.0.3'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2'

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```

### firebase-database-kotlin
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-database-kotlin/badge.svg)

Kotlin support module for `firebase-database`

```groovy
// firebase-database
implementation 'com.androidhuman.rxfirebase2:firebase-database-kotlin:16.0.3.0'
implementation 'com.androidhuman.rxfirebase2:firebase-database:16.0.3.0'
implementation 'com.google.firebase:firebase-database:16.0.3'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2''

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```

### firebase-firestore
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-firestore/badge.svg)

RxJava binding APIs for [Firebase Firestore](https://firebase.google.com/docs/firestore/) Android SDK.

```groovy
// firebase-firestore
implementation 'com.androidhuman.rxfirebase2:firebase-firestore:17.1.5.0'
implementation 'com.google.firebase:firebase-firestore:17.1.5'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2'

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```

### firebase-firestore-kotlin
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-firestore-kotlin/badge.svg)

Kotlin support module for `firebase-firestore`.

```groovy
// firebase-firestore
implementation 'com.androidhuman.rxfirebase2:firebase-firestore-kotlin:17.1.5.0'
implementation 'com.androidhuman.rxfirebase2:firebase-firestore:17.1.5.0'
implementation 'com.google.firebase:firebase-firestore:17.1.5'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2'

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```

### firebase-storage (Coming soon!)
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-storage/badge.svg)

RxJava binding APIs for [Firebase Storage](https://firebase.google.com/docs/storage/) Android SDK.

```groovy
// firebase-storage
implementation 'com.androidhuman.rxfirebase2:firebase-storage:16.0.1.0'
implementation 'com.google.firebase:firebase-storage:16.0.1'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2'

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```

### firebase-storage-kotlin (Coming soon!)
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-storage-kotlin/badge.svg)

Kotlin support module for `firebase-storage`.

```groovy
// firebase-storage
implementation 'com.androidhuman.rxfirebase2:firebase-storage-kotlin:16.0.1.0'
implementation 'com.androidhuman.rxfirebase2:firebase-storage:16.0.1.0'
implementation 'com.google.firebase:firebase-storage:16.0.1'

// required dependency: firebase-core
implementation 'com.androidhuman.rxfirebase2:firebase-core:17.2.2.0'
implementation 'com.google.firebase:firebase-core:17.2.2'

// required dependency: rxjava2
implementation 'io.reactivex.rxjava2:rxjava:2.1.8'
```


## Kotlin extension modules

Each kotlin support module maps all methods in Java module into an extension function on following classes:

Basically, extension function has same name of methods in `RxXXX` classes in Java module.

If extension function conflicts with a method in a class that is being extended, it will be renamed with `rx` prefix.

For more details, please refer to following 'Usage' section.

## Usage

- [Firebase Authentication](https://github.com/kunny/RxFirebase/wiki/Authentication)
- [Firebase Realtime Database](https://github.com/kunny/RxFirebase/wiki/Realtime-Database)

See [official documentation](https://firebase.google.com/docs/) for the details.

## Versioning

RxFirebase uses a versioning rule that is related to corresponding Firebase's version by following rule:

```
RxFirebaseVersion : {major}.{minor}.{patch1}.{patch2} =
    {Firebase major}.{Firebase minor}.{Firebase patch}.{RxFirebase patch}
```

For example, a library version that depends on `10.2.0` version of Firebase SDK, whose patch version is `1` will be `10.2.0.1`.

## License

```
Copyright 2016-2018 Taeho Kim <jyte82@gmail.com>

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
