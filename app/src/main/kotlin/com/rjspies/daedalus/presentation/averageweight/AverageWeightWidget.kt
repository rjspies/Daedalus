package com.rjspies.daedalus.presentation.averageweight

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.rjspies.daedalus.R
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.darkScheme
import com.rjspies.daedalus.presentation.history.asUserfacingString
import com.rjspies.daedalus.presentation.lightScheme
import org.koin.java.KoinJavaComponent.inject

class AverageWeightWidget : GlanceAppWidget() {
    private val viewModel by inject<AverageWeightWidgetViewModel>(AverageWeightWidgetViewModel::class.java)

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val colors = ColorProviders(
                light = lightScheme,
                dark = darkScheme,
            )
            GlanceTheme(colors) {
                val glanceWidgetTextStyle = TextStyle(
                    color = GlanceTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                )
                val averageWeight by viewModel.averageWeight.collectAsState()

                Content(context, glanceWidgetTextStyle, averageWeight)
            }
        }
    }

    @Composable
    private fun Content(
        context: Context,
        textStyle: TextStyle,
        averageWeight: Float,
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(Spacings.S)
                .background(GlanceTheme.colors.background),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        ) {
            Text(
                text = context.getString(R.string.average_weight_widget_average_weight_caption),
                style = textStyle.copy(fontSize = MaterialTheme.typography.labelLarge.fontSize),
                maxLines = 2,
            )
            Text(
                text = averageWeight.asUserfacingString(context.resources.configuration.locales[0]),
                style = textStyle.copy(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                maxLines = 1,
            )
        }
    }
}
