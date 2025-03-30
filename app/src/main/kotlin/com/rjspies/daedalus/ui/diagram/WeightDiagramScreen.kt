package com.rjspies.daedalus.ui.diagram

//import com.rjspies.daedalus.data.WeightChartEntry
import android.graphics.Typeface
import android.text.Layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape
import com.patrykandpatrick.vico.core.common.shape.MarkerCorneredShape
import com.ramcosta.composedestinations.annotation.Destination
import com.rjspies.daedalus.R
import com.rjspies.daedalus.data.WeightChartEntry
import com.rjspies.daedalus.ui.MainNavigationGraph
import com.rjspies.daedalus.ui.common.EmptyScreen
import com.rjspies.daedalus.ui.common.horizontalSpacingM
import com.rjspies.daedalus.ui.common.verticalSpacingM
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Destination<MainNavigationGraph>(start = true)
@Composable
fun WeightDiagramScreen(
    viewModel: WeightDiagramViewModel = koinViewModel(),
    scaffoldPadding: PaddingValues,
) {
    val weights by viewModel.weights.collectAsState()
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
        }
    } else {
        EmptyScreen(
            painter = painterResource(R.drawable.chart_line_fill),
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
//    val lineProducer = remember(entries) { ChartEntryModelProducer(entries) }
//    val axisFormatter = remember { WeightDateAxisFormatter }
//    val valuesOverrider = remember(entries) { AxisValuesOverrider.fixed(minY = 0f, maxY = entries.maxOf { it.y } * 1.1f) }
//    val axisLabel = axisLabelComponent()
    val vicoScrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End)
//    val persistentMarker = rememberPersistentMarker(entries.indices)
//
    LaunchedEffect(entries) {
        vicoScrollState.animateScroll(Scroll.Relative.x(vicoScrollState.maxValue.toDouble()))
    }

    val modelProducer = remember { CartesianChartModelProducer() }
    val textComponent = rememberTextComponent(
        color = MaterialTheme.colorScheme.onTertiary,
        background = rememberShapeComponent(
            fill = Fill(MaterialTheme.colorScheme.tertiary.toArgb()),
            shape = MarkerCorneredShape(CorneredShape.Corner.Rounded),
        ),
        padding = Insets(
            horizontalDp = 8f,
            verticalDp = 2f,
        ),
        typeface = Typeface.MONOSPACE,
        textAlignment = Layout.Alignment.ALIGN_CENTER,
    )
    val shapeComponent = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.tertiary.toArgb()),
        shape = CorneredShape.Pill,
        strokeThickness = 6.dp,
    )
    val lineComponent = rememberLineComponent(
        fill = Fill(MaterialTheme.colorScheme.tertiary.toArgb()),
        shape = DashedShape(CorneredShape.Pill),
    )

    LaunchedEffect(entries) {
        modelProducer.runTransaction {
            lineSeries {
                series(x = entries.map { it.x }, y = entries.map { it.y })
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = { _, value, _ ->
                    entries[value.toInt()].dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                },
                itemPlacer = HorizontalAxis.ItemPlacer.segmented(),
            ),
            layers = arrayOf(rememberLineCartesianLayer()),
            marker = remember {
                DefaultCartesianMarker(
                    label = textComponent,
                    indicator = { shapeComponent },
                    guideline = lineComponent,
                )
            },
            fadingEdges = remember { FadingEdges() },
        ),
        modelProducer = modelProducer,
        scrollState = vicoScrollState,
    )

//    com.patrykandpatrick.vico.compose.chart.Chart(
//        chart = lineChart(
//            axisValuesOverrider = valuesOverrider,
//            persistentMarkers = persistentMarker,
//        ),
//        chartModelProducer = lineProducer,
//        startAxis = rememberStartAxis(label = axisLabel),
//        bottomAxis = rememberBottomAxis(
//            valueFormatter = axisFormatter,
//            label = axisLabel,
//        ),
//        marker = rememberMarker(),
//        isZoomEnabled = true,
//        runInitialAnimation = false,
//        chartScrollState = chartScrollState,
//        chartScrollSpec = rememberChartScrollSpec(initialScroll = InitialScroll.End),
//    )
}

//@Composable
//private fun rememberPersistentMarker(indices: IntRange): Map<Float, Marker> {
//    val label = axisLabelComponent()
//    val indicator = shapeComponent(
//        shape = Shapes.pillShape,
//        color = MaterialTheme.colorScheme.secondary,
//    )
//    val guideline = axisGuidelineComponent()
//
//    return remember(indices) {
//        if (indices.count() == 1) {
//            indices.associate {
//                it.toFloat() to object : MarkerComponent(
//                    label = label,
//                    indicator = indicator,
//                    guideline = guideline,
//                ) {
//                    init {
//                        indicatorSizeDp = INDICATOR_SIZE_DP
//                    }
//                }
//            }
//        } else {
//            emptyMap()
//        }
//    }
//}

private const val INDICATOR_SIZE_DP = 6f
