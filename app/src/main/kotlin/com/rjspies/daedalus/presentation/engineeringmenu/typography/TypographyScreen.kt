package com.rjspies.daedalus.presentation.engineeringmenu.typography

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.util.fastForEach
import com.rjspies.daedalus.R.string
import com.rjspies.daedalus.presentation.DaedalusTheme
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.SubScreenContent
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.verticalSpacingL

@Composable
@PreviewLightDark
fun TypographyScreenPreview() {
    DaedalusTheme {
        TypographyScreen(onNavigateUp = {})
    }
}

@Composable
fun TypographyScreen(onNavigateUp: () -> Unit) {
    SubScreenContent(stringResource(string.engineering_menu_typography_screen_title), onNavigateUp) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding)
                .verticalSpacingL(),
            verticalArrangement = Arrangement.spacedBy(Spacings.L),
        ) {
            remember { EngineeringTextStyle.entries }.fastForEach { textStyle ->
                TextStyleSection(textStyle)
            }
        }
    }
}

@Composable
private fun TextStyleSection(style: EngineeringTextStyle) {
    Column {
        Text(
            text = style.title,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.horizontalSpacingM(),
        )
        HorizontalDivider()
        Text(
            text = style.text,
            style = style.style().copy(hyphens = Hyphens.Auto),
            modifier = Modifier.horizontalSpacingM(),
        )
    }
}

private enum class EngineeringTextStyle(
    open val title: String,
    open val style: @Composable () -> TextStyle,
    val text: String = "Der Hase springt über den faulen Hund.",
) {
    DisplayLarge(
        title = "displayLarge",
        style = { MaterialTheme.typography.displayLarge },
    ),
    DisplayMedium(
        title = "displayMedium",
        style = { MaterialTheme.typography.displayMedium },
    ),
    DisplaySmall(
        title = "displaySmall",
        style = { MaterialTheme.typography.displaySmall },
    ),
    HeadlineLarge(
        title = "headlineLarge",
        style = { MaterialTheme.typography.headlineLarge },
    ),
    HeadlineMedium(
        title = "headlineMedium",
        style = { MaterialTheme.typography.headlineMedium },
    ),
    HeadlineSmall(
        title = "headlineSmall",
        style = { MaterialTheme.typography.headlineSmall },
    ),
    TitleLarge(
        title = "titleLarge",
        style = { MaterialTheme.typography.titleLarge },
    ),
    TitleMedium(
        title = "titleMedium",
        style = { MaterialTheme.typography.titleMedium },
    ),
    TitleSmall(
        title = "titleSmall",
        style = { MaterialTheme.typography.titleSmall },
    ),
    BodyLarge(
        title = "bodyLarge",
        style = { MaterialTheme.typography.bodyLarge },
    ),
    BodyMedium(
        title = "bodyMedium",
        style = { MaterialTheme.typography.bodyMedium },
    ),
    BodySmall(
        title = "bodySmall",
        style = { MaterialTheme.typography.bodySmall },
    ),
    LabelLarge(
        title = "labelLarge",
        style = { MaterialTheme.typography.labelLarge },
    ),
    LabelMedium(
        title = "labelMedium",
        style = { MaterialTheme.typography.labelMedium },
    ),
    LabelSmall(
        title = "labelSmall",
        style = { MaterialTheme.typography.labelSmall },
    ),
}
