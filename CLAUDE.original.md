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

# Run lint (detekt with type resolution)
./gradlew detektMain detektTest

# Print version name
./gradlew version

# Print version code
./gradlew versionCode
```

## Architecture

The app follows a strict three-layer clean architecture enforced by `ArchitectureLayerTest`:

- **`domain`** — Pure Kotlin interfaces and use cases. No dependencies on `data` or `presentation`. Contains: `Weight` (interface), `WeightService` (interface), `SnackbarRepository` (interface), `CoroutineDispatcherProvider` (interface), `DefaultCoroutineDispatcherProvider`, `SnackbarVisuals`, use cases (`InsertWeightUseCase`, `DeleteWeightUseCase`, `GetWeightsAscendingUseCase`, `GetWeightsDescendingUseCase`, `ExportWeightsUseCase`, `ImportWeightsUseCase`, `ShowSnackbarUseCase`, `GetSnackbarUseCase`), and `WeightTableDefinition` constants.
- **`data`** — Room database implementations. Depends on `domain` only. Contains: `WeightDatabase`, `WeightDao`, `WeightImpl`, `WeightServiceImpl`, `SnackbarRepositoryImpl`, `ZonedDateTimeConverter`.
- **`presentation`** — Jetpack Compose UI. Depends on `domain` only, never on `data`. Contains screens (`WeightDiagramScreen`, `WeightHistoryScreen`, `MainScreen`), ViewModels (`MainViewModel`, `WeightDiagramViewModel`, `WeightHistoryViewModel`, `WeightHistoryItemViewModel`), and common UI components.

Dependency injection is handled by Koin with a single `APP_MODULE` in `koin/AppModule.kt`.

## Key Technical Details

- **DI**: Koin (`APP_MODULE` in `koin/AppModule.kt`) — uses `singleOf`, `factoryOf`, `viewModelOf`
- **Database**: Room with KSP code generation; schemas exported to `app/schemas/`; auto-migration from version 1 to 2
- **Navigation**: Jetpack Navigation Compose with typed, serializable routes (`Route` sealed class)
- **Testing**: JUnit 5 + Kotest assertions + ArchUnit for architecture checks + Koin test utilities + Kotlin Coroutines Test
- **Static analysis**: Detekt with type resolution — use `./gradlew detektMain detektTest` (not the plain `detekt` task). Config at `config/detekt/detekt.yml`; variant-specific baselines at `config/detekt/baseline-debug.xml` and `config/detekt/baseline-release.xml`; regenerate with `detektBaselineDebug` / `detektBaselineRelease`. `autoCorrect = true` is enabled
- **Kotlin compiler**: `allWarningsAsErrors = true` — all warnings must be resolved
- **Version management**: `gradle/libs.versions.toml` (version catalog); dependency updates via `./gradlew refreshVersions` (stable-only)
- **Icons**: Material Icons Extended (`androidx.compose.material:material-icons-extended`)
- **Vertical spacing**: Use `VerticalSpacerXxx()` composable functions from `VerticalSpacerExtensions.kt` (e.g. `VerticalSpacerM()`) — not raw `Spacer(Modifier.height(...))`
- **Horizontal spacing**: Use `HorizontalSpacerXxx()` composable functions from `HorizontalSpacerExtensions.kt` (e.g. `HorizontalSpacerM()`) — not raw `Spacer(Modifier.width(...))`

## Screens / Navigation Routes

- `Route.Diagram` → `WeightDiagramScreen` — weight chart using Vico
- `Route.History` → `WeightHistoryScreen` — list of weight entries with delete support

## Coding Conventions

- **No abbreviations**: Do not use abbreviations in names (variables, functions, classes, parameters, etc.). Use full words instead. Globally established acronyms and abbreviations are exempt — examples: JSON, CSV, HTML, XML, URL, API, ID, UI.

## Git Workflow

- **Branch naming**: Bugfix branches are prefixed with `Bugfix/`; all other branches (features, chores, version bumps, etc.) are prefixed with `Feature/`. After the slash: first word capitalized, remaining words lowercase, words separated by hyphens. Example: `Feature/My-test-feature`
- **Pull requests**: After completing a task on a branch, always create a PR against `main` — or push to the existing branch if a PR already exists. Always assign `rjspies` as the assignee (`gh pr create ... --assignee rjspies`)
