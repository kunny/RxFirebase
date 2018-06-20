# Change Log

## RxFirebase-Auth, RxFirebase-Database Version 16.0.1.1 *(2018/06/20)*

Built and tested with:
- Firebase Authentication Android SDK 16.0.1
- Firebase Realtime Database Android SDK 16.0.1
- Kotlin 1.2.41

Breaking changes:
- Your app gradle file now has to explicitly list `com.androidhuman.rxfirebase2:firebase-core` as a dependency for RxFirebase to work as expected.

Fixed:
- Missing artifact dependency (#37)

## RxFirebase-Auth Version 16.0.1.0 *(2018/06/09)*

Built and tested with:
- Firebase Authentication Android SDK 16.0.1
- Kotlin 1.2.41

Removed:
- `RxFirebaseUser.getToken()`

## RxFirebase-Database Version 16.0.1.0 *(2018/06/09)*

Built and tested with:
- Firebase Realtime Database Android SDK 16.0.1
- Kotlin 1.2.41

## RxFirebase-Auth Version 15.1.0.0 *(2018/06/09)*

Built and tested with:
- Firebase Authentication Android SDK 15.1.0
- Kotlin 1.2.41

Added:
- `RxFirebaseAuth.fetchSignInMethodsForEmail()`

Deprecated:
- `RxFirebaseAuth.fetchProvidersForEmail()`

## Version 15.0.0.0 *(2018/06/06)*

Built and tested with:
- Firebase Android SDK 15.0.0
- Kotlin 1.2.41

## Version 12.0.1.0 *(2018/06/06)*

Built and tested with:
- Firebase Android SDK 12.0.1
- Kotlin 1.2.41

## Version 12.0.0.0 *(2018/06/06)*

Built and tested with:
- Firebase Android SDK 12.0.0
- Kotlin 1.2.41

Added:
- `RxFirebaseAuth.signInWithEmailLink()`

## Version 11.8.0.1 *(2018/02/26)*

Built and tested with:
- Firebase Android SDK 11.8.0
- Kotlin 1.2.21

Added:
- `RxFirebaseUser.updatePhoneNumber()`

New bindings:
- `RxPhoneAuthProvider` for `PhoneAuthProvider`


## Version 11.8.0.0 *(2018/01/18)*

Built and tested with:
- Firebase Android SDK 11.8.0
- Kotlin 1.2.20

Breaking changes:
- `RxFirebaseAuth.fetchProvidersForEmail()` now returns `Maybe`.
- Function name change: `RxFirebaseAuth.signInAnonymous()` -> `RxFirebaseAuth.signInAnonymously()`
- Minor API changes on `DataValue`, `Some`
- Added `Flowable` support on `RxFirebaseDatabase`:
  - `childEvents()`
  - `dataChanges()`
  - `dataChangesOf()`

Updated:
- Rewrote `XXOnSubscribe` classes into custom `Observer` classes. (#12)

## Version 11.6.0.1 *(2017/11/24)*

Built and tested with:
- Firebase Android SDK 11.6.0
- Kotlin 1.1.60

Added:
- Add binding for `FirebaseUser.getIdToken()` (#23)

Updated:
- Android Gradle Plugin 3.0.1
- Build tools version 27.0.1
- Kotlin 1.1.60

## Version 11.6.0.0 *(2017/11/13)*

Built and tested with:
- Firebase Android SDK 11.6.0
- Kotlin 1.1.4-3

## Version 11.4.2.0 *(2017/11/13)*

Built and tested with:
- Firebase Android SDK 11.4.2
- Kotlin 1.1.4-3

## Version 11.4.0.0 *(2017/11/13)*

Built and tested with:
- Firebase Android SDK 11.4.0
- Kotlin 1.1.4-3

## Version 11.2.2.0 *(2017/09/19)*

- Updated Firebase SDK version to 11.2.2.

## Version 11.2.0.0 *(2017/09/19)*

- Updated Firebase SDK version to 11.2.0.
- Updated Kotlin to 1.1.4-3.
- Updated RxJava to 2.1.3.
- Updated compileSdkVersion to 26.

## Version 11.0.4.0 *(2017/07/30)*

- Updated Firebase SDK version to 11.0.4.

## Version 11.0.2.0 *(2017/07/30)*

- Updated Firebase SDK version to 11.0.2.

## Version 11.0.1.0 *(2017/07/30)*

- Updated Firebase SDK version to 11.0.1.

## Version 11.0.0.0 *(2017/07/30)*

- Updated Firebase SDK version to 11.0.0.
- Updated Android Plugin for Gradle to 2.3.3.
- Updated Kotlin to 1.1.2-3.
- Updated Build tools version to 26.0.1.
- Removed support annotations from dependency.

## Version 10.2.6.0 *(2017/05/20)*

Update Firebase SDK version to 10.2.6.

## Version 10.2.4.0 *(2017/05/14)*

Update Firebase SDK version to 10.2.4.

## Version 10.2.1.0 *(2017/05/14)*

- Update Firebase SDK version to 10.2.1.
- Update Kotlin version to 1.1.2.

## Version 10.2.0.0 *(2017/04/06)*

### Breaking changes

- Updated library version format
- Updated `RxJava` dependency to `2.0.7`
  - Thank you for the initial RxJava2 PR by [b3er](https://github.com/b3er)!
- Updated `Kotlin` dependency to `1.1.1`
- Removed `TaskResult`, `Optional` from usage.
- `Task<Void>` type is converted to `Completable`
- single-value emitting Observable is converted to `Single`

### SDK version changes

Update Firebase SDK version to 10.2.0.

## Version 10.1.0 *(2017/01/17)*

Update Firebase SDK version to 10.0.1.

## Version 10.0.0 *(2017/01/17)*

Update Firebase SDK version to 10.0.0.

## Version 9.80.0 *(2016/10/27)*

Update Firebase SDK version to 9.8.0.

## Version 9.61.0 *(2016/10/11)*

Initial release.
