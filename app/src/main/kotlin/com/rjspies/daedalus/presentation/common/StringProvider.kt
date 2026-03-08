package com.rjspies.daedalus.presentation.common

import androidx.annotation.StringRes

fun interface StringProvider {
    fun getString(@StringRes resId: Int): String
}
