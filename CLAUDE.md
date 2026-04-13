# CLAUDE.md

File gives guidance to Claude Code (claude.ai/code) when working in this repo.

## Project Overview

Daedalus = Android weight-tracking app. Jetpack Compose, API 29+ (minSdk 29, targetSdk 35). Clean arch, layer deps enforced by ArchUnit.

## Build & Development Commands

All commands use Gradle wrapper from project root:

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

App follows strict three-layer clean arch enforced by `ArchitectureLayerTest`:

- **`domain`** — Pure Kotlin interfaces + use cases. No deps on `data` or `presentation`. Contains: `Weight` (interface), `WeightService` (interface), `SnackbarRepository` (interface), `CoroutineDispatcherProvider` (interface), `DefaultCoroutineDispatcherProvider`, `SnackbarVisuals`, use cases (`InsertWeightUseCase`, `DeleteWeightUseCase`, `GetWeightsAscendingUseCase`, `GetWeightsDescendingUseCase`, `ExportWeightsUseCase`, `ImportWeightsUseCase`, `ShowSnackbarUseCase`, `GetSnackbarUseCase`), `WeightTableDefinition` constants.
- **`data`** — Room DB implementations. Depends on `domain` only. Contains: `WeightDatabase`, `WeightDao`, `WeightImpl`, `WeightServiceImpl`, `SnackbarRepositoryImpl`, `ZonedDateTimeConverter`.
- **`presentation`** — Jetpack Compose UI. Depends on `domain` only, never `data`. Contains screens (`WeightDiagramScreen`, `WeightHistoryScreen`, `MainScreen`), ViewModels (`MainViewModel`, `WeightDiagramViewModel`, `WeightHistoryViewModel`, `WeightHistoryItemViewModel`), common UI components.

DI via Koin, single `APP_MODULE` in `koin/AppModule.kt`.

## Key Technical Details

- **DI**: Koin (`APP_MODULE` in `koin/AppModule.kt`) — uses `singleOf`, `factoryOf`, `viewModelOf`
- **Database**: Room + KSP; schemas exported to `app/schemas/`; auto-migration v1→v2
- **Navigation**: Jetpack Navigation Compose, typed serializable routes (`Route` sealed class)
- **Testing**: JUnit 5 + Kotest assertions + ArchUnit + Koin test utils + Kotlin Coroutines Test
- **Static analysis**: Detekt with type resolution — use `./gradlew detektMain detektTest` (not plain `detekt`). Config: `config/detekt/detekt.yml`; variant baselines: `config/detekt/baseline-debug.xml`, `config/detekt/baseline-release.xml`; regenerate with `detektBaselineDebug` / `detektBaselineRelease`. `autoCorrect = true` enabled
- **Kotlin compiler**: `allWarningsAsErrors = true` — all warnings must resolve
- **Version management**: `gradle/libs.versions.toml`; updates via `./gradlew refreshVersions` (stable-only)
- **Icons**: Material Icons Extended (`androidx.compose.material:material-icons-extended`)
- **Vertical spacing**: Use `VerticalSpacerXxx()` composables from `VerticalSpacerExtensions.kt` (e.g. `VerticalSpacerM()`) — not raw `Spacer(Modifier.height(...))`
- **Horizontal spacing**: Use `HorizontalSpacerXxx()` composables from `HorizontalSpacerExtensions.kt` (e.g. `HorizontalSpacerM()`) — not raw `Spacer(Modifier.width(...))`

## Screens / Navigation Routes

- `Route.Diagram` → `WeightDiagramScreen` — weight chart via Vico
- `Route.History` → `WeightHistoryScreen` — weight entry list with delete

## Coding Conventions

- **No abbreviations**: Full words in all names (variables, functions, classes, parameters). Globally established acronyms exempt — e.g. JSON, CSV, HTML, XML, URL, API, ID, UI.

## Git Workflow

- **Branch naming**: Bugfix branches prefixed `Bugfix/`; all others (features, chores, version bumps, etc.) prefixed `Feature/`. After slash: first word capitalized, rest lowercase, hyphen-separated. Example: `Feature/My-test-feature`
- **Pull requests**: After task complete, always create PR against `main` — or push to existing branch if PR exists. Always assign `rjspies` (`gh pr create ... --assignee rjspies`)