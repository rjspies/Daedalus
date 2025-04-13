package com.rjspies.daedalus.ui.widgets

import android.content.Context
import androidx.compose.material3.MaterialTheme
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
import com.rjspies.daedalus.ui.common.Spacings
import com.rjspies.daedalus.ui.darkScheme
import com.rjspies.daedalus.ui.history.asUserfacingString
import com.rjspies.daedalus.ui.lightScheme
import org.koin.java.KoinJavaComponent.inject

class AverageWeightWidget : GlanceAppWidget() {
    private val viewModel: AverageWeightWidgetViewModel by inject(AverageWeightWidgetViewModel::class.java)

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
                        style = glanceWidgetTextStyle.copy(fontSize = MaterialTheme.typography.labelSmall.fontSize),
                        maxLines = 2,
                    )
                    Text(
                        text = averageWeight.asUserfacingString(context.resources.configuration.locales[0]),
                        style = glanceWidgetTextStyle.copy(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
