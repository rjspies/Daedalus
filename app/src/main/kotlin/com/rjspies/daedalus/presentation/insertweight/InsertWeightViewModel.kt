package com.rjspies.daedalus.presentation.insertweight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rjspies.daedalus.InsertWeightError
import com.rjspies.daedalus.domain.InsertWeightUseCase
import com.rjspies.daedalus.presentation.common.SAVED_STATE_HANDLE_KEY_UI_STATE
import java.time.ZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class InsertWeightViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val insertWeightUseCase: InsertWeightUseCase,
) : ViewModel() {
    val uiState: StateFlow<InsertWeightUiState> = savedStateHandle.getStateFlow(
        key = SAVED_STATE_HANDLE_KEY_UI_STATE,
        initialValue = InsertWeightUiState(),
    )

    fun setDismissDialog(dismiss: Boolean) {
        savedStateHandle[SAVED_STATE_HANDLE_KEY_UI_STATE] = uiState.value.copy(dismissDialog = dismiss)
    }

    fun setError(error: InsertWeightError?) {
        savedStateHandle[SAVED_STATE_HANDLE_KEY_UI_STATE] = uiState.value.copy(error = error)
    }

    private fun setIsLoading(isLoading: Boolean) {
        savedStateHandle[SAVED_STATE_HANDLE_KEY_UI_STATE] = uiState.value.copy(isLoading = isLoading)
    }

    fun insertWeight(weightValue: String) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            setError(null)
            setIsLoading(true)
            val parsedWeightValue = weightValue.parseToFloat()
            if (parsedWeightValue != null) {
                insertWeightUseCase(parsedWeightValue, null, ZonedDateTime.now())
                setDismissDialog(true)
            } else {
                setError(InsertWeightError.ParseFloatError)
            }
        }

        job.invokeOnCompletion {
            setIsLoading(false)
        }
    }
}

private fun String.parseToFloat(): Float? = replace(",", ".").toFloatOrNull()
