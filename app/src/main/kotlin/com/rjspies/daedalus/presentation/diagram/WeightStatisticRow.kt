package com.rjspies.daedalus.presentation.diagram

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rjspies.daedalus.R
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.horizontalSpacingM

@Composable
internal fun WeightStatisticRow(
    thirtyDayAverage: Float?,
    latestWeight: Float?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalSpacingM(),
        horizontalArrangement = Arrangement.spacedBy(Spacings.XS),
    ) {
        WeightStatisticItem(
            label = stringResource(R.string.weight_diagram_statistic_thirty_day_average_label),
            value = thirtyDayAverage,
            modifier = Modifier.weight(1f),
        )
        WeightStatisticItem(
            label = stringResource(R.string.weight_diagram_statistic_latest_weight_label),
            value = latestWeight,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun WeightStatisticItem(
    label: String,
    value: Float?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(Spacings.M)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
        )
        if (value != null) {
            val unitStyle = MaterialTheme.typography.bodySmall.toSpanStyle()
            Text(
                text = buildAnnotatedString {
                    append("%.1f".format(value))
                    withStyle(unitStyle) { append(" kg") }
                },
                style = MaterialTheme.typography.headlineMedium,
            )
        } else {
            Text(
                text = "\u2012",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

private data class WeightStatisticRowPreviewData(
    val thirtyDayAverage: Float?,
    val latestWeight: Float?,
)

private class WeightStatisticRowPreviewProvider :
    PreviewParameterProvider<WeightStatisticRowPreviewData> {
    override val values = sequenceOf(
        WeightStatisticRowPreviewData(thirtyDayAverage = 82.5f, latestWeight = 85.0f),
        WeightStatisticRowPreviewData(thirtyDayAverage = null, latestWeight = null),
    )
}

@PreviewLightDark
@Composable
private fun WeightStatisticRowPreview(
    @PreviewParameter(WeightStatisticRowPreviewProvider::class)
    data: WeightStatisticRowPreviewData,
) {
    WeightStatisticRow(
        thirtyDayAverage = data.thirtyDayAverage,
        latestWeight = data.latestWeight,
    )
}
