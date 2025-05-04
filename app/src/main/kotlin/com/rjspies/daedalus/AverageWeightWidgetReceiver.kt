package com.rjspies.daedalus

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.rjspies.daedalus.presentation.averageweight.AverageWeightWidget

class AverageWeightWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = AverageWeightWidget()
}
