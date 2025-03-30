package com.rjspies.daedalus.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.spec.TypedDestinationSpec

@Composable
fun NavigationBar(
    currentDestination: TypedDestinationSpec<*>,
    navigate: (destination: DirectionDestinationSpec) -> Unit,
) {
    NavigationBar {
        val entries = remember(TopLevelDestination.entries) { TopLevelDestination.entries }
        entries.forEach {
            NavigationBarItem(
                selected = currentDestination == it.destination,
                onClick = { navigate(it.destination) },
                label = { Text(stringResource(it.labelResourceId)) },
                icon = {
                    Icon(
                        painter = painterResource(it.iconResourceId),
                        contentDescription = stringResource(it.labelResourceId),
                    )
                },
            )
        }
    }
}
