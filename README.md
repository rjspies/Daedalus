### Architecture

Daedalus follows a strict three-layer clean architecture:

- **`domain`** — Pure Kotlin interfaces and use cases. No dependencies on `data` or `presentation`.
- **`data`** — Room database implementations. Depends on `domain` only.
- **`presentation`** — Jetpack Compose UI. Depends on `domain` only, never on `data`.

Layer dependency rules are enforced automatically by [ArchUnit](https://www.archunit.org/) tests.

### Icons

This app uses [Material Icons Extended](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary) (Rounded style).
