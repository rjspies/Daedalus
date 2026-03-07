# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Daedalus is an Android weight-tracking app built with Jetpack Compose, targeting API 29+ (minSdk 29, targetSdk 35). It uses a clean architecture with strict layer dependency rules enforced by ArchUnit tests.

## Build & Development Commands

All commands use the Gradle wrapper from the project root:

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (requires env vars: DAEDALUS_STORE_PASSWORD, DAEDALUS_SIGNING_KEY, DAEDALUS_SIGNING_KEY_PASSWORD)
./gradlew assembleRelease

# Run all unit tests
./gradlew test

# Run tests for a specific build variant
./gradlew testDebugUnitTest
./gradlew testReleaseUnitTest

# Run lint (detekt)
./gradlew detekt

# Print version name
./gradlew version

# Print version code
./gradlew versionCode
```

## Architecture

The app follows a strict three-layer clean architecture enforced by `ArchitectureLayerKonsistTest`:

- **`domain`** — Pure Kotlin interfaces and use cases. No dependencies on `data` or `presentation`. Contains: `Weight` (interface), use cases (`InsertWeightUseCase`, `DeleteWeightUseCase`, `GetWeightsAscendingUseCase`, `GetWeightsDescendingUseCase`, `ExportWeightsUseCase`), `WeightService` interface, `SnackbarRepository` interface, `CoroutineDispatcherProvider` interface, and `WeightTableDefinition` constants.
- **`data`** — Room database implementations. Depends on `domain` only. Contains: `WeightDatabase`, `WeightDao`, `WeightServiceImpl`, `SnackbarRepositoryImpl`, `ZonedDateTimeConverter`.
- **`presentation`** — Jetpack Compose UI. Depends on `domain` only, never on `data`. Contains screens (`WeightDiagramScreen`, `WeightHistoryScreen`, `MainScreen`), ViewModels, and common UI components.

Dependency injection is handled by Koin with a single `APP_MODULE` in `koin/AppModule.kt`.

## Key Technical Details

- **DI**: Koin (`APP_MODULE` in `koin/AppModule.kt`) — uses `singleOf`, `factoryOf`, `viewModelOf`
- **Database**: Room with KSP code generation; schemas exported to `app/schemas/`
- **Navigation**: Jetpack Navigation Compose with typed routes (`Route` sealed class)
- **Testing**: JUnit 5 + Kotest assertions + Konsist for architecture checks + Koin test utilities
- **Static analysis**: Detekt with config at `config/detekt/detekt.yml` and baselines at `config/detekt/baseline.xml`; `autoCorrect = true` is enabled
- **Kotlin compiler**: `allWarningsAsErrors = true` — all warnings must be resolved
- **Version management**: `gradle/libs.versions.toml` (version catalog); dependency updates via `./gradlew refreshVersions` (stable-only)
- **Icons**: Material Icons Extended (`androidx.compose.material:material-icons-extended`)

## Screens / Navigation Routes

- `Route.Diagram` → `WeightDiagramScreen` — weight chart using Vico
- `Route.History` → `WeightHistoryScreen` — list of weight entries with delete support
