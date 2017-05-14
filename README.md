# RxFirebase
[![CircleCI](https://circleci.com/gh/kunny/RxFirebase.svg?style=shield)](https://circleci.com/gh/kunny/RxFirebase)
[![Coverage Status](https://coveralls.io/repos/github/kunny/RxFirebase/badge.svg?branch=rxjava2)](https://coveralls.io/github/kunny/RxFirebase?branch=rxjava2)
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.androidhuman.rxfirebase2/firebase-auth/badge.svg)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxFirebase-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4496)

RxJava binding APIs for [Firebase](https://firebase.google.com/) Android SDK.

## RxJava Version

Currently, it depends on RxJava `2.0.7`.

For RxJava1 compatible version, see [rxjava1](https://github.com/kunny/RxFirebase/tree/rxjava1) branch.

## Modules

### firebase-auth

RxJava binding APIs for [Firebase Authentication](https://firebase.google.com/docs/auth/).

```groovy
compile 'com.google.firebase:firebase-auth:10.2.1'
compile 'com.androidhuman.rxfirebase2:firebase-auth:10.2.1.0'
compile 'io.reactivex.rxjava2:rxjava:2.0.7'
```

### firebase-auth-kotlin

Kotlin support module for `firebase-auth`.

```groovy
compile 'com.google.firebase:firebase-auth:10.2.1'
compile 'com.androidhuman.rxfirebase2:firebase-auth-kotlin:10.2.1.0'
compile 'io.reactivex.rxjava2:rxjava:2.0.7'
```

### firebase-database

RxJava binding APIs for [Firebase Realtime Database](https://firebase.google.com/docs/database/) Android SDK.

```groovy
compile 'com.google.firebase:firebase-database:10.2.1'
compile 'com.androidhuman.rxfirebase2:firebase-database:10.2.1.0'
compile 'io.reactivex.rxjava2:rxjava:2.0.7'
```

### firebase-database-kotlin

Kotlin support module for `firebase-database`

```groovy
compile 'com.google.firebase:firebase-database:10.2.1'
compile 'com.androidhuman.rxfirebase2:firebase-database-kotlin:10.2.1.0'
compile 'io.reactivex.rxjava2:rxjava:2.0.7'
```

Each kotlin support module maps all methods in Java module into an extension function on following classes:

- firebase-auth-kotlin
  - `FirebaseAuth`
  - `FirebaseUser`
- firebase-database-kotlin
  - `DatabaseReference`
  - `Query`

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

## Development Snapshot

Snapshots of the development version are available in [Sonatype snapshots](https://oss.sonatype.org/content/repositories/snapshots/) repository.

You can register snapshots repo as your project's remote repo as following:

```groovy
repositories {
    ... other remote repositories ...

    // Add following line
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

Currently no snapshot version is available.

## License

```
Copyright 2016-2017 Taeho Kim <jyte82@gmail.com>

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
