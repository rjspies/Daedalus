package com.rjspies.daedalus.domain

import java.time.ZonedDateTime

abstract class Weight {
    abstract val id: Int
    abstract val value: Float
    abstract val note: String?
    abstract val dateTime: ZonedDateTime
}
