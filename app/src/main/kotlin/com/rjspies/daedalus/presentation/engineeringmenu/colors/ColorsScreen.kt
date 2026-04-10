package com.rjspies.daedalus.presentation.engineeringmenu.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.rjspies.daedalus.R.string
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.SubScreenContent
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.verticalSpacingL

@Composable
fun ColorsScreen(onNavigateUp: () -> Unit) {
    SubScreenContent(stringResource(string.engineering_menu_typography_screen_title), onNavigateUp) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding)
                .verticalSpacingL(),
            verticalArrangement = Arrangement.spacedBy(Spacings.XS),
        ) {
            remember { EngineeringColor.entries }.fastForEach { textStyle ->
                ColorSection(textStyle)
            }
        }
    }
}

@Composable
private fun ColorSection(color: EngineeringColor) {
    Row(
        modifier = Modifier.horizontalSpacingM(),
        horizontalArrangement = Arrangement.spacedBy(Spacings.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp)
                .background(color.color()),
        )
        Column {
            Text(
                text = color.title,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
            )
            Text(
                text = "#${color.color().toArgb().toHexString(HexFormat.UpperCase)}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
            )
        }
    }
}

private enum class EngineeringColor(
    open val title: String,
    open val color: @Composable () -> Color,
) {
    // Primary
    Primary(
        title = "primary",
        color = { MaterialTheme.colorScheme.primary },
    ),
    OnPrimary(
        title = "onPrimary",
        color = { MaterialTheme.colorScheme.onPrimary },
    ),
    PrimaryContainer(
        title = "primaryContainer",
        color = { MaterialTheme.colorScheme.primaryContainer },
    ),
    OnPrimaryContainer(
        title = "onPrimaryContainer",
        color = { MaterialTheme.colorScheme.onPrimaryContainer },
    ),
    InversePrimary(
        title = "inversePrimary",
        color = { MaterialTheme.colorScheme.inversePrimary },
    ),
    PrimaryFixed(
        title = "primaryFixed",
        color = { MaterialTheme.colorScheme.primaryFixed },
    ),
    PrimaryFixedDim(
        title = "primaryFixedDim",
        color = { MaterialTheme.colorScheme.primaryFixedDim },
    ),
    OnPrimaryFixed(
        title = "onPrimaryFixed",
        color = { MaterialTheme.colorScheme.onPrimaryFixed },
    ),
    OnPrimaryFixedVariant(
        title = "onPrimaryFixedVariant",
        color = { MaterialTheme.colorScheme.onPrimaryFixedVariant },
    ),

    // Secondary
    Secondary(
        title = "secondary",
        color = { MaterialTheme.colorScheme.secondary },
    ),
    OnSecondary(
        title = "onSecondary",
        color = { MaterialTheme.colorScheme.onSecondary },
    ),
    SecondaryContainer(
        title = "secondaryContainer",
        color = { MaterialTheme.colorScheme.secondaryContainer },
    ),
    OnSecondaryContainer(
        title = "onSecondaryContainer",
        color = { MaterialTheme.colorScheme.onSecondaryContainer },
    ),
    SecondaryFixed(
        title = "secondaryFixed",
        color = { MaterialTheme.colorScheme.secondaryFixed },
    ),
    SecondaryFixedDim(
        title = "secondaryFixedDim",
        color = { MaterialTheme.colorScheme.secondaryFixedDim },
    ),
    OnSecondaryFixed(
        title = "onSecondaryFixed",
        color = { MaterialTheme.colorScheme.onSecondaryFixed },
    ),
    OnSecondaryFixedVariant(
        title = "onSecondaryFixedVariant",
        color = { MaterialTheme.colorScheme.onSecondaryFixedVariant },
    ),

    // Tertiary
    Tertiary(
        title = "tertiary",
        color = { MaterialTheme.colorScheme.tertiary },
    ),
    OnTertiary(
        title = "onTertiary",
        color = { MaterialTheme.colorScheme.onTertiary },
    ),
    TertiaryContainer(
        title = "tertiaryContainer",
        color = { MaterialTheme.colorScheme.tertiaryContainer },
    ),
    OnTertiaryContainer(
        title = "onTertiaryContainer",
        color = { MaterialTheme.colorScheme.onTertiaryContainer },
    ),
    TertiaryFixed(
        title = "tertiaryFixed",
        color = { MaterialTheme.colorScheme.tertiaryFixed },
    ),
    TertiaryFixedDim(
        title = "tertiaryFixedDim",
        color = { MaterialTheme.colorScheme.tertiaryFixedDim },
    ),
    OnTertiaryFixed(
        title = "onTertiaryFixed",
        color = { MaterialTheme.colorScheme.onTertiaryFixed },
    ),
    OnTertiaryFixedVariant(
        title = "onTertiaryFixedVariant",
        color = { MaterialTheme.colorScheme.onTertiaryFixedVariant },
    ),

    // Other
    Error(
        title = "error",
        color = { MaterialTheme.colorScheme.error },
    ),
    OnError(
        title = "onError",
        color = { MaterialTheme.colorScheme.onError },
    ),
    ErrorContainer(
        title = "errorContainer",
        color = { MaterialTheme.colorScheme.errorContainer },
    ),
    OnErrorContainer(
        title = "onErrorContainer",
        color = { MaterialTheme.colorScheme.onErrorContainer },
    ),
    Outline(
        title = "outline",
        color = { MaterialTheme.colorScheme.outline },
    ),
    OutlineVariant(
        title = "outlineVariant",
        color = { MaterialTheme.colorScheme.outlineVariant },
    ),
    Background(
        title = "background",
        color = { MaterialTheme.colorScheme.background },
    ),
    OnBackground(
        title = "onBackground",
        color = { MaterialTheme.colorScheme.onBackground },
    ),
    Surface(
        title = "surface",
        color = { MaterialTheme.colorScheme.surface },
    ),
    OnSurface(
        title = "onSurface",
        color = { MaterialTheme.colorScheme.onSurface },
    ),
    InverseSurface(
        title = "inverseSurface",
        color = { MaterialTheme.colorScheme.inverseSurface },
    ),
    InverseOnSurface(
        title = "inverseOnSurface",
        color = { MaterialTheme.colorScheme.inverseOnSurface },
    ),
    SurfaceBright(
        title = "surfaceBright",
        color = { MaterialTheme.colorScheme.surfaceBright },
    ),
    SurfaceDim(
        title = "surfaceDim",
        color = { MaterialTheme.colorScheme.surfaceDim },
    ),
    SurfaceContainer(
        title = "surfaceContainer",
        color = { MaterialTheme.colorScheme.surfaceContainer },
    ),
    SurfaceContainerLow(
        title = "surfaceContainerLow",
        color = { MaterialTheme.colorScheme.surfaceContainerLow },
    ),
    SurfaceContainerLowest(
        title = "surfaceContainerLowest",
        color = { MaterialTheme.colorScheme.surfaceContainerLowest },
    ),
    SurfaceContainerHigh(
        title = "surfaceContainerHigh",
        color = { MaterialTheme.colorScheme.surfaceContainerHigh },
    ),
    SurfaceContainerHighest(
        title = "surfaceContainerHighest",
        color = { MaterialTheme.colorScheme.surfaceContainerHighest },
    ),
}
