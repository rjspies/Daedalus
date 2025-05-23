package com.rjspies.daedalus.presentation.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalFoundationApi
internal inline fun <T> LazyListScope.tableItems(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> contentType(items[index]) },
) {
    HorizontalDivider(Modifier.animateItem())
    itemContent(items[it])
    if (it >= items.lastIndex) HorizontalDivider(Modifier.animateItem())
}
