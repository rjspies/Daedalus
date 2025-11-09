package com.rjspies.daedalus.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.rjspies.daedalus.R

@Composable
fun Snackbar(
    message: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onDismissRequest: () -> Unit = {},
) {
    val colors = provideColors(isError)
    Snackbar(
        modifier = Modifier
            .padding(Spacings.M)
            .then(modifier),
        shape = MaterialTheme.shapes.medium,
        dismissAction = {
            IconButton(
                onClick = onDismissRequest,
                content = {
                    Icon(
                        painter = rememberVectorPainter(Icons.Rounded.Close),
                        contentDescription = stringResource(R.string.common_snackbar_dismiss_action_icon_content_description),
                    )
                },
            )
        },
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
        actionContentColor = colors.actionContentColor,
        dismissActionContentColor = colors.dismissActionContentColor,
        content = { Text(message) },
    )
}

@Composable
private fun provideColors(isError: Boolean): SnackbarColors {
    return if (isError) {
        SnackbarColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            actionContentColor = MaterialTheme.colorScheme.onErrorContainer,
            dismissActionContentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
    } else {
        SnackbarColors(
            containerColor = SnackbarDefaults.color,
            contentColor = SnackbarDefaults.contentColor,
            actionContentColor = SnackbarDefaults.actionContentColor,
            dismissActionContentColor = SnackbarDefaults.dismissActionContentColor,
        )
    }
}

private data class SnackbarColors(
    val containerColor: Color,
    val contentColor: Color,
    val actionContentColor: Color,
    val dismissActionContentColor: Color,
)
