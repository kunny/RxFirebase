# Releasing

 1. Change the version in `gradle.properties` to the next release version.
 2. Update the `CHANGELOG.md` for the impending release.
 3. Update the `README.md` with the new version.
 4. `git commit -am "Prepare for release X.Y.Z."` (where X.Y.Z is the new version)
 5. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version)
 6. `./gradlew clean uploadArchives`
 7. `git push && git push --tags`
 8. Visit [Sonatype Nexus](https://oss.sonatype.org/) and promote the artifact.
