package com.rjspies.daedalus.domain

import java.time.ZonedDateTime

interface Weight {
    val id: Int
    val value: Float
    val note: String?
    val dateTime: ZonedDateTime
}
