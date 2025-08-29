package com.rjspies.daedalus.presentation.diagram

import android.graphics.Typeface
import android.text.Layout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Addchart
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonGhost
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.dialog.AlertDialog
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.progress.LinearProgressIndicatorIndeterminate
import com.adevinta.spark.components.spacer.Spacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.icons.SparkIcon
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.FadingEdges
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape
import com.patrykandpatrick.vico.core.common.shape.MarkerCorneredShape
import com.rjspies.daedalus.R
import com.rjspies.daedalus.presentation.common.EmptyScreen
import com.rjspies.daedalus.presentation.common.HorizontalSpacerXS
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.VerticalSpacerL
import com.rjspies.daedalus.presentation.common.VerticalSpacerXS
import com.rjspies.daedalus.presentation.common.WeightChartEntry
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.verticalSpacingM
import com.rjspies.daedalus.presentation.navigation.Route
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSparkApi::class)
@Suppress("LongMethod")
@Composable
fun WeightDiagramScreen(
    scaffoldPadding: PaddingValues,
    navigate: (Route) -> Unit,
    viewModel: WeightDiagramViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(scaffoldPadding)
            .padding(bottom = Spacings.XXL),
    ) {
        if (uiState.shouldShowInsertWeightDialog) {
            val focusRequester = remember { FocusRequester() }
            AlertDialog(
                onDismissRequest = {
                    if (uiState.isInsertWeightDialogDismissable) {
                        viewModel.onEvent(WeightDiagramViewModel.Event.CloseInsertWeightDialog)
                    }
                },
                confirmButton = {
                    ButtonGhost(
                        onClick = { viewModel.onEvent(WeightDiagramViewModel.Event.InsertCurrentWeight) },
                        content = {
                            Text(stringResource(R.string.insert_weight_insert_button_text))
                        },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                icon = { Icon(rememberVectorPainter(Icons.Rounded.Addchart), contentDescription = null) },
                title = { Text(stringResource(R.string.insert_weight_dialog_title)) },
                text = {
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                    Column {
                        TextField(
                            value = uiState.insertWeightDialogCurrentWeight.orEmpty(),
                            onValueChange = { viewModel.onEvent(WeightDiagramViewModel.Event.SetCurrentWeight(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            label = stringResource(R.string.insert_weight_weight_text_field_label),
                            helper = if (uiState.insertWeightDialogError != null) {
                                stringResource(R.string.insert_weight_weight_text_field_supporting_message_error)
                            } else {
                                stringResource(R.string.insert_weight_weight_text_field_supporting_message)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { viewModel.onEvent(WeightDiagramViewModel.Event.InsertCurrentWeight) },
                            ),
                            state = TextFieldState.Error.takeIf { uiState.insertWeightDialogError != null },
                        )

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
                                if (uiState.isInsertWeightDialogLoading) {
                                    VerticalSpacerXS()
                                    LinearProgressIndicatorIndeterminate(modifier = Modifier.fillMaxWidth())
                                }
                            },
                        )
                    }
                },
            )
        }

        if (uiState.weights.isNotEmpty()) {
            Column {
                Text(
                    text = stringResource(R.string.weight_diagram_title),
                    modifier = Modifier.horizontalSpacingM(),
                    style = SparkTheme.typography.display2,
                )
                Box(Modifier.horizontalSpacingM()) { Chart(uiState.weights) }
            }
            Spacer(Modifier.weight(1f))
        } else {
            Spacer(Modifier.weight(1f))
            EmptyScreen(
                painter = rememberVectorPainter(Icons.Rounded.Timeline),
                contentDescription = stringResource(R.string.weight_diagram_empty_screen_content_description),
                title = stringResource(R.string.weight_diagram_empty_screen_title),
                subtitle = stringResource(R.string.weight_diagram_empty_screen_subtitle),
                modifier = Modifier
                    .verticalSpacingM()
                    .horizontalSpacingM(),
            )
            Spacer(Modifier.weight(1f))
        }

        VerticalSpacerL()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalSpacingM(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ButtonOutlined(
                onClick = { navigate(Route.History) },
                modifier = Modifier.weight(1f),
                icon = SparkIcon.Vector(Icons.AutoMirrored.Rounded.List),
                content = { Text(stringResource(R.string.weight_diagram_button_history_title)) },
            )
            HorizontalSpacerXS()
            ButtonFilled(
                onClick = { viewModel.onEvent(WeightDiagramViewModel.Event.ShowInsertWeightDialog) },
                modifier = Modifier.weight(1f),
                icon = SparkIcon.Vector(Icons.Rounded.Add),
                content = { Text(stringResource(R.string.weight_diagram_button_insert_weight_title)) },
            )
        }
    }
}

@Composable
private fun Chart(entries: List<WeightChartEntry>) {
    val vicoScrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End)
    val modelProducer = remember { CartesianChartModelProducer() }
    val axisText = rememberAxisText()
    val markerText = rememberMarkerText()
    val markerShape = rememberMarkerShape()
    val markerLine = rememberMarkerLine()
    val maxY = remember(entries) { entries.map { it.y }.average() * 2.0 }

    LaunchedEffect(entries) {
        modelProducer.runTransaction {
            lineSeries {
                series(x = entries.map { it.x }, y = entries.map { it.y })
            }
        }
    }

    LaunchedEffect(entries) {
        vicoScrollState.animateScroll(Scroll.Relative.x(vicoScrollState.maxValue.toDouble()))
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            startAxis = VerticalAxis.rememberStart(
                label = axisText,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = axisText,
                valueFormatter = { _, value, _ ->
                    if (value.toInt() in entries.indices) {
                        entries[value.toInt()]
                    } else {
                        entries.last()
                    }.dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                },
                itemPlacer = HorizontalAxis.ItemPlacer.segmented(),
            ),
            layers = arrayOf(
                rememberLineCartesianLayer(
                    rangeProvider = CartesianLayerRangeProvider.fixed(maxY = maxY),
                ),
            ),
            marker = remember {
                DefaultCartesianMarker(
                    label = markerText,
                    indicator = { markerShape },
                    guideline = markerLine,
                )
            },
            fadingEdges = remember { FadingEdges() },
        ),
        modelProducer = modelProducer,
        scrollState = vicoScrollState,
    )
}

@Composable
private fun rememberMarkerShape() = rememberShapeComponent(
    fill = Fill(SparkTheme.colors.support.toArgb()),
    shape = CorneredShape.Pill,
    strokeThickness = 6.dp,
)

@Composable
private fun rememberMarkerText() = rememberTextComponent(
    color = SparkTheme.colors.onSupport,
    background = rememberShapeComponent(
        fill = Fill(Color.Transparent.toArgb()),
        strokeFill = Fill(SparkTheme.colors.support.toArgb()),
        strokeThickness = 1.dp,
        shape = MarkerCorneredShape(CorneredShape.Corner.Rounded),
    ),
    padding = Insets(
        horizontalDp = 8f,
        verticalDp = 2f,
    ),
    typeface = Typeface.MONOSPACE,
    textAlignment = Layout.Alignment.ALIGN_CENTER,
)

@Composable
private fun rememberMarkerLine() = rememberLineComponent(
    fill = Fill(SparkTheme.colors.support.toArgb()),
    shape = DashedShape(CorneredShape.Pill),
)

@Composable
private fun rememberAxisText() = rememberTextComponent(
    color = SparkTheme.colors.onBackground,
)
