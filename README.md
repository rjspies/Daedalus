# Daedalus

[![Get it on Obtainium](https://raw.githubusercontent.com/ImranR98/Obtainium/main/assets/graphics/badge_obtainium.png)](https://github.com/rjspies/Daedalus/releases/latest)

Daedalus is a minimalist Android app for tracking body weight. Entries are saved with a timestamp, visualised as a chart, and can be exported and imported as CSV. No accounts, no cloud, no ads.

## Installation

[Obtainium](https://github.com/ImranR98/Obtainium) lets you install and update
Daedalus directly from GitHub releases.

1. Open Obtainium and tap **Add App**
2. Enter the repository URL: `https://github.com/rjspies/Daedalus`
3. Under **APK Filter Regex**, enter: `Daedalus-.*\.apk`
4. Tap **Add**

### Verify a release

Each release includes a SHA-256 checksum (`.sha256`) and its GPG signature
(`.sha256.sig`). To verify:

```shell
# Import the developer's public key (once)
curl https://raw.githubusercontent.com/rjspies/Daedalus/main/app/miscellaneous/rjspies.asc | gpg --import

# Verify the checksum was signed by the developer
gpg --verify Daedalus-<version>-<versionCode>.apk.sha256.sig \
             Daedalus-<version>-<versionCode>.apk.sha256

# Verify the APK matches the checksum
sha256sum -c Daedalus-<version>-<versionCode>.apk.sha256
```

## Development

### Architecture

Daedalus follows a strict three-layer clean architecture:

- **`domain`** — Pure Kotlin interfaces and use cases. No dependencies on `data` or `presentation`.
- **`data`** — Room database implementations. Depends on `domain` only.
- **`presentation`** — Jetpack Compose UI. Depends on `domain` only, never on `data`.

Layer dependency rules are enforced automatically by [ArchUnit](https://www.archunit.org/) tests.

### Icons

This app uses [Material Icons Extended](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary) (Rounded style).

### Build

```shell
# Build debug APK
./gradlew assembleDebug

# Run all unit tests
./gradlew test

# Run lint (detekt)
./gradlew detekt
```
