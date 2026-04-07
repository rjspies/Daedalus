package com.rjspies.daedalus.presentation.diagram

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.rjspies.daedalus.R
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.horizontalSpacingM

@Composable
internal fun WeightStatRow(
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
        WeightStatCard(
            label = stringResource(R.string.weight_diagram_stat_thirty_day_average_label),
            value = thirtyDayAverage,
            modifier = Modifier.weight(1f),
        )
        WeightStatCard(
            label = stringResource(R.string.weight_diagram_stat_latest_weight_label),
            value = latestWeight,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun WeightStatCard(
    label: String,
    value: Float?,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(Spacings.M)) {
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
                    text = "\u2014",
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}
