package com.rjspies.daedalus.presentation.importexport

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rjspies.daedalus.presentation.common.Spacings
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImportExportScreen(scaffoldPadding: PaddingValues, viewModel: ImportExportViewModel = koinViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(scaffoldPadding)
            .padding(bottom = Spacings.XXL),
        content = {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val exportData = uiState.exportPrompt

            key(exportData) {
                if (exportData != null) {
                    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(exportData.mimeType)) {
                        viewModel.onEvent(ImportExportViewModel.Event.PathChosen(it))
                    }

                    LaunchedEffect(Unit) {
                        launcher.launch(exportData.fileName)
                    }
                }
            }

            Text("Import export")
            Button({ viewModel.onEvent(ImportExportViewModel.Event.Export) }) {
                Text("Export")
            }
            OutlinedButton({ viewModel.onEvent(ImportExportViewModel.Event.Import) }) {
                Text("Import")
            }
        }
    )
}