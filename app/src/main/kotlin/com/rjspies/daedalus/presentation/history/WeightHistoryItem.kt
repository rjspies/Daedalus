package com.rjspies.daedalus.presentation.history

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingFlat
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rjspies.daedalus.R
import com.rjspies.daedalus.domain.Weight
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.VerticalSpacerXS
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import org.koin.androidx.compose.koinViewModel

@Suppress("LongMethod")
@Composable
fun WeightHistoryItem(
    weight: Weight,
    predecessorWeight: Weight?,
    modifier: Modifier = Modifier,
    viewModel: WeightHistoryItemViewModel = koinViewModel(key = weight.id.toString()),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.shouldShowDialog) {
        AlertDialog(
            onDismissRequest = {
                if (uiState.isDialogDismissable) {
                    viewModel.onEvent(WeightHistoryItemViewModel.Event.HideDialog)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(WeightHistoryItemViewModel.Event.DeleteWeight(weight)) },
                    content = { Text(stringResource(R.string.weight_history_dialog_delete_item_button)) },
                )
            },
            modifier = Modifier.fillMaxWidth(),
            title = { Text(stringResource(R.string.weight_history_dialog_delete_item_title)) },
            text = {
                Column {
                    Text(stringResource(R.string.weight_history_dialog_delete_item_message))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(
                                animationSpec = tween(
                                    durationMillis = 35,
                                    easing = LinearEasing,
                                ),
                            ),
                        content = {
                            if (uiState.isDialogLoading) {
                                VerticalSpacerXS()
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            }
                        },
                    )
                }
            },
        )
    }

    ElevatedCard(
        content = {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(modifier),
                content = {
                    val (avatar, title, date, noteReference, deleteButton) = createRefs()
                    val locale = LocalConfiguration.current.locales[0]
                    val note = weight.note
                    val arrowState = when {
                        predecessorWeight?.value == null -> ArrowState.Neutral
                        weight.value > predecessorWeight.value -> ArrowState.Upwards
                        weight.value < predecessorWeight.value -> ArrowState.Downwards
                        else -> ArrowState.Neutral
                    }

                    Avatar(
                        state = arrowState,
                        modifier = Modifier.constrainAs(avatar) {
                            top.linkTo(parent.top, margin = Spacings.M)
                            start.linkTo(parent.start, margin = Spacings.M)
                            bottom.linkTo(parent.bottom, margin = Spacings.M)
                        },
                    )
                    val unitStyle = MaterialTheme.typography.bodySmall.toSpanStyle()
                    Text(
                        text = buildAnnotatedString {
                            append("%.1f".format(weight.value))
                            withStyle(unitStyle) { append(" kg") }
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.constrainAs(title) {
                            width = Dimension.fillToConstraints
                            top.linkTo(parent.top, margin = Spacings.M)
                            start.linkTo(avatar.end, margin = Spacings.M)
                            end.linkTo(deleteButton.start, margin = Spacings.M)
                        },
                    )
                    Text(
                        text = weight.dateTime.asUserfacingString(locale),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.constrainAs(date) {
                            width = Dimension.fillToConstraints
                            top.linkTo(title.bottom)
                            start.linkTo(avatar.end, margin = Spacings.M)
                            end.linkTo(deleteButton.start, margin = Spacings.M)

                            if (note.isNullOrBlank()) {
                                bottom.linkTo(parent.bottom, margin = Spacings.M)
                            }
                        },
                    )

                    if (!note.isNullOrBlank()) {
                        Text(
                            text = stringResource(R.string.weight_history_item_note, note),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.constrainAs(noteReference) {
                                width = Dimension.fillToConstraints
                                top.linkTo(date.bottom)
                                start.linkTo(avatar.end, margin = Spacings.M)
                                bottom.linkTo(parent.bottom, margin = Spacings.M)
                                end.linkTo(deleteButton.start, margin = Spacings.M)
                            },
                        )
                    }

                    IconButton(
                        onClick = { viewModel.onEvent(WeightHistoryItemViewModel.Event.ShowDialog) },
                        modifier = Modifier.constrainAs(deleteButton) {
                            top.linkTo(parent.top, margin = Spacings.M)
                            end.linkTo(parent.end, margin = Spacings.M)
                            bottom.linkTo(parent.bottom, margin = Spacings.M)
                        },
                        content = {
                            Icon(
                                painter = rememberVectorPainter(Icons.Rounded.DeleteForever),
                                contentDescription = stringResource(R.string.weight_history_delete_icon_content_description),
                            )
                        },
                    )
                },
            )
        },
    )
}

@Composable
private fun Avatar(
    state: ArrowState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.then(modifier),
        content = {
            Icon(
                painter = rememberVectorPainter(state.imageVector),
                contentDescription = state.contentDescription(),
                modifier = Modifier.padding(Spacings.S),
            )
        },
    )
}

private sealed class ArrowState(val imageVector: ImageVector) {
    data object Neutral : ArrowState(Icons.AutoMirrored.Rounded.TrendingFlat)
    data object Downwards : ArrowState(Icons.AutoMirrored.Rounded.TrendingDown)
    data object Upwards : ArrowState(Icons.AutoMirrored.Rounded.TrendingUp)
}

@ReadOnlyComposable
@Composable
private fun ArrowState.contentDescription(): String = when (this) {
    ArrowState.Downwards -> stringResource(R.string.weight_history_avatar_downwards_icon_content_description)
    ArrowState.Neutral -> stringResource(R.string.weight_history_avatar_neutral_icon_content_description)
    ArrowState.Upwards -> stringResource(R.string.weight_history_avatar_upwards_icon_content_description)
}

private fun ZonedDateTime.asUserfacingString(
    locale: Locale,
    style: FormatStyle = FormatStyle.MEDIUM,
) = format(DateTimeFormatter.ofLocalizedDateTime(style).withLocale(locale))
