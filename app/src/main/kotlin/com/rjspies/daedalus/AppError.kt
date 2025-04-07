package com.rjspies.daedalus

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface AppError : Parcelable

@Parcelize
sealed class InsertWeightError : AppError {
    data object ParseFloatError : InsertWeightError()
}
