package com.rjspies.daedalus

private const val PREFIX = "com.rjspies.daedalus.intent.action"

sealed class IntentActions(
    val action: String,
) {
    data object AddWeight : IntentActions("$PREFIX.ADD_WEIGHT")
}
