package com.rjspies.daedalus.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme

@Composable
fun DaedalusTheme(content: @Composable () -> Unit) {
    SparkTheme(
        colors = if (isSystemInDarkTheme()) {
            darkSparkColors()
        } else {
            lightSparkColors()
        },
        content = {
            ProvideVicoTheme(
                theme = rememberM3VicoTheme(
                    lineCartesianLayerColors = listOf(SparkTheme.colors.main),
                    lineColor = SparkTheme.colors.main.copy(alpha = .3f),
                    textColor = SparkTheme.colors.onBackground,
                ),
                content = content,
            )
        },
    )
}
