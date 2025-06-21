package com.rjspies.daedalus.presentation.diagram

import android.graphics.Typeface
import android.text.Layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.rjspies.daedalus.presentation.common.VerticalSpacerL
import com.rjspies.daedalus.presentation.common.WeightChartEntry
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.verticalSpacingM
import com.rjspies.daedalus.presentation.navigation.Route
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun WeightDiagramScreen(
    scaffoldPadding: PaddingValues,
    navigate: (Route) -> Unit,
    viewModel: WeightDiagramViewModel = koinViewModel(),
) {
    val weights by viewModel.weights.collectAsStateWithLifecycle()
    val entries = rememberSaveable(weights) {
        weights.mapIndexed { index, weight ->
            WeightChartEntry(
                x = index.toFloat(),
                y = weight.value,
                dateTime = weight.dateTime,
            )
        }
    }

    if (entries.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding),
        ) {
            Text(
                text = stringResource(R.string.weight_diagram_title),
                modifier = Modifier.horizontalSpacingM(),
                style = MaterialTheme.typography.headlineMedium,
            )
            Box(Modifier.horizontalSpacingM()) { Chart(entries) }
            VerticalSpacerL()
            Button(
                onClick = { navigate(Route.History) },
                modifier = Modifier
                    .horizontalSpacingM()
                    .align(Alignment.End),
                shape = ShapeDefaults.Large,
            ) {
                Text(stringResource(R.string.weight_diagram_button_history_title))
            }
        }
    } else {
        EmptyScreen(
            painter = rememberVectorPainter(Icons.Rounded.Timeline),
            contentDescription = stringResource(R.string.weight_diagram_empty_screen_content_description),
            title = stringResource(R.string.weight_diagram_empty_screen_title),
            subtitle = stringResource(R.string.weight_diagram_empty_screen_subtitle),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding)
                .verticalSpacingM()
                .horizontalSpacingM(),
        )
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
    fill = Fill(MaterialTheme.colorScheme.tertiary.toArgb()),
    shape = CorneredShape.Pill,
    strokeThickness = 6.dp,
)

@Composable
private fun rememberMarkerText() = rememberTextComponent(
    color = MaterialTheme.colorScheme.onTertiary,
    background = rememberShapeComponent(
        fill = Fill(Color.Transparent.toArgb()),
        strokeFill = Fill(MaterialTheme.colorScheme.tertiary.toArgb()),
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
    fill = Fill(MaterialTheme.colorScheme.tertiary.toArgb()),
    shape = DashedShape(CorneredShape.Pill),
)

@Composable
private fun rememberAxisText() = rememberTextComponent(
    color = MaterialTheme.colorScheme.onBackground,
)
