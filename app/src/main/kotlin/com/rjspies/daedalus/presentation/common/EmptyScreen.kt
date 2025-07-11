package com.rjspies.daedalus.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.text.Text

@Composable
fun EmptyScreen(
    painter: Painter,
    contentDescription: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(48.dp),
            )
            VerticalSpacerM()
            Text(
                text = title,
                style = SparkTheme.typography.display2,
                textAlign = TextAlign.Center,
            )
            Text(
                text = subtitle,
                textAlign = TextAlign.Center,
            )
        },
    )
}
