package com.rjspies.daedalus.presentation.diagram

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Addchart
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.FadingEdges
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.DashedShape
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.rjspies.daedalus.R
import com.rjspies.daedalus.presentation.common.EmptyScreen
import com.rjspies.daedalus.presentation.common.OverviewScreenContent
import com.rjspies.daedalus.presentation.common.VerticalSpacerM
import com.rjspies.daedalus.presentation.common.VerticalSpacerXS
import com.rjspies.daedalus.presentation.common.WeightChartEntry
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.verticalSpacingL
import com.rjspies.daedalus.presentation.common.verticalSpacingM
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale
import org.koin.androidx.compose.koinViewModel

private const val FULL_CORNER_RADIUS_PERCENT = 50
private const val CHART_Y_AXIS_PADDING = 5f
private const val CHART_SINGLE_ENTRY_X_MIN = -0.5
private const val CHART_SINGLE_ENTRY_X_MAX = 0.5

@Suppress("LongMethod")
@Composable
fun WeightDiagramScreen(
    onOpenDrawer: () -> Unit,
    viewModel: WeightDiagramViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isMenuExpanded by remember { mutableStateOf(false) }

    OverviewScreenContent(
        title = stringResource(R.string.navigation_top_bar_title_diagram),
        onOpenDrawer = onOpenDrawer,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(WeightDiagramViewModel.Event.ShowInsertWeightDialog) },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.weight_diagram_floating_action_button_content_description),
                )
            }
        },
        actions = {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = stringResource(R.string.weight_diagram_overflow_menu_content_description),
                )
            }
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.weight_diagram_button_import_weights_title)) },
                    onClick = {
                        isMenuExpanded = false
                        viewModel.onEvent(WeightDiagramViewModel.Event.ImportClicked)
                    },
                    enabled = !uiState.isImporting,
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.weight_diagram_button_export_weights_title)) },
                    onClick = {
                        isMenuExpanded = false
                        viewModel.onEvent(WeightDiagramViewModel.Event.ExportClicked)
                    },
                    enabled = !uiState.isExporting,
                )
            }
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding)
                .verticalSpacingL(),
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
                        TextButton(
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
                            OutlinedTextField(
                                value = uiState.insertWeightDialogCurrentWeight.orEmpty(),
                                onValueChange = { viewModel.onEvent(WeightDiagramViewModel.Event.SetCurrentWeight(it)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                label = { Text(stringResource(R.string.insert_weight_weight_text_field_label)) },
                                shape = MaterialTheme.shapes.medium,
                                supportingText = {
                                    if (uiState.insertWeightDialogError != null) {
                                        Text(stringResource(R.string.insert_weight_weight_text_field_supporting_message_error))
                                    } else {
                                        Text(stringResource(R.string.insert_weight_weight_text_field_supporting_message))
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done,
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { viewModel.onEvent(WeightDiagramViewModel.Event.InsertCurrentWeight) },
                                ),
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
                                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                    }
                                },
                            )
                        }
                    },
                )
            }

            val exportData = uiState.exportPrompt
            if (exportData != null) {
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(exportData.mimeType)) {
                    viewModel.onEvent(WeightDiagramViewModel.Event.PathChosen(it?.toString()))
                }

                LaunchedEffect(Unit) {
                    launcher.launch(exportData.fileName)
                }
            }

            val importData = uiState.importPrompt
            if (importData != null) {
                val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
                    viewModel.onEvent(WeightDiagramViewModel.Event.ImportPathChosen(it?.toString()))
                }

                LaunchedEffect(Unit) {
                    importLauncher.launch(arrayOf(importData.mimeType))
                }
            }

            if (uiState.weights.isNotEmpty()) {
                Box(Modifier.horizontalSpacingM()) { Chart(uiState.weights) }
                VerticalSpacerM()
                WeightStatisticRow(
                    thirtyDayAverage = uiState.thirtyDayAverageWeight,
                    latestWeight = uiState.latestWeight,
                )
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
        }
    }
}

@Composable
private fun Chart(entries: List<WeightChartEntry>) {
    val vicoScrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End)
    val modelProducer = remember { CartesianChartModelProducer() }
    val colorScheme = MaterialTheme.colorScheme
    val axisText = rememberAxisText()
    val bottomAxisText = rememberBottomAxisText()
    val bottomAxisFormatter = remember(entries) {
        CartesianValueFormatter { _, value, _ ->
            val entry = entries.getOrNull(value.toInt())
            entry?.let { e ->
                val currentYear = Year.now().value
                val pattern = if (e.dateTime.year == currentYear) "d. MMM" else "d. MMM yy"
                e.dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
            }.orEmpty()
        }
    }
    val markerText = rememberMarkerText()
    val markerShape = rememberMarkerShape()
    val markerLine = rememberMarkerLine()
    val minY = remember(entries) { (entries.minOf { it.y } - CHART_Y_AXIS_PADDING).coerceAtLeast(0f).toDouble() }
    val maxY = remember(entries) { (entries.maxOf { it.y } + CHART_Y_AXIS_PADDING).toDouble() }
    val isSingleEntry = entries.size == 1
    val lineSpec = rememberLineSpec(colorScheme.primary)

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
                guideline = null,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = bottomAxisText,
                labelRotationDegrees = -45f,
                valueFormatter = bottomAxisFormatter,
            ),
            layers = arrayOf(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(lineSpec),
                    rangeProvider = CartesianLayerRangeProvider.fixed(
                        minX = if (isSingleEntry) CHART_SINGLE_ENTRY_X_MIN else null,
                        maxX = if (isSingleEntry) CHART_SINGLE_ENTRY_X_MAX else null,
                        minY = minY,
                        maxY = maxY,
                    ),
                ),
            ),
            marker = remember(entries, markerText, markerShape, markerLine) {
                DefaultCartesianMarker(
                    label = markerText,
                    indicator = { markerShape },
                    guideline = markerLine,
                    valueFormatter = DefaultCartesianMarker.ValueFormatter { _, targets ->
                        formatMarkerLabel(entries, targets)
                    },
                )
            },
            fadingEdges = remember { FadingEdges() },
        ),
        modelProducer = modelProducer,
        scrollState = vicoScrollState,
    )
}

@Composable
private fun rememberLineSpec(color: Color): LineCartesianLayer.Line {
    val pointComponent = rememberShapeComponent(fill = Fill(color), shape = CircleShape)
    return LineCartesianLayer.rememberLine(
        fill = remember(color) { LineCartesianLayer.LineFill.single(Fill(color)) },
        stroke = remember { LineCartesianLayer.LineStroke.Continuous(thickness = 2.dp) },
        areaFill = remember(color) {
            LineCartesianLayer.AreaFill.single(
                Fill(Brush.verticalGradient(listOf(color.copy(alpha = 0.3f), Color.Transparent))),
            )
        },
        interpolator = remember { LineCartesianLayer.Interpolator.cubic() },
        pointProvider = remember(pointComponent) {
            LineCartesianLayer.PointProvider.single(LineCartesianLayer.Point(pointComponent, size = 6.dp))
        },
    )
}

@Composable
private fun rememberMarkerShape() = rememberShapeComponent(
    fill = Fill(MaterialTheme.colorScheme.tertiary),
    shape = CircleShape,
)

@Composable
private fun rememberMarkerText() = rememberTextComponent(
    style = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        textAlign = TextAlign.Center,
    ),
    background = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.tertiaryContainer),
        shape = MarkerCornerBasedShape(RoundedCornerShape(FULL_CORNER_RADIUS_PERCENT)),
    ),
    padding = Insets(
        horizontal = 8.dp,
        vertical = 2.dp,
    ),
)

@Composable
private fun rememberMarkerLine() = rememberLineComponent(
    fill = Fill(MaterialTheme.colorScheme.tertiary),
    shape = DashedShape(CircleShape),
)

@Composable
private fun rememberAxisText() = rememberTextComponent(
    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onBackground),
)

@Composable
private fun rememberBottomAxisText() = rememberTextComponent(
    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onBackground),
)

private fun formatMarkerLabel(
    entries: List<WeightChartEntry>,
    targets: List<CartesianMarker.Target>,
): String {
    val target = targets.firstOrNull()
    val entry = target?.let { entries.getOrNull(it.x.toInt()) }
    return entry?.let { e ->
        val currentYear = Year.now().value
        val pattern = if (e.dateTime.year == currentYear) "d. MMM" else "d. MMM yy"
        "${String.format(Locale.getDefault(), "%.1f", e.y)}\n${e.dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))}"
    }.orEmpty()
}
