package com.rjspies.daedalus

interface AppError

sealed class InsertWeightError : AppError {
    data object ParseFloatError : InsertWeightError()
}
