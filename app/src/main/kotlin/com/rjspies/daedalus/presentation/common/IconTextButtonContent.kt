package com.rjspies.daedalus.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.text.Text

@Composable
fun IconTextButtonContent(text: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.then(modifier),
        horizontalArrangement = Arrangement.Absolute.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(rememberVectorPainter(icon), null)
        HorizontalSpacerXS()
        Text(text)
    }
}
