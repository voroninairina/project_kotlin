package com.example.project.cartoons.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.project.cartoons.presentation.model.CartoonsSettingsState
import com.example.project.cartoons.presentation.viewModel.CartoonsSettingsViewModel
import com.example.project.uikit.Spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartoonsSettingsDialog() {
    val viewModel = koinViewModel<CartoonsSettingsViewModel>()

    val state by viewModel.viewState.collectAsStateWithLifecycle()

    CartoonsSettingsDialog(
        state,
        viewModel::onAliveFirstCheckedChange,
        viewModel::onBack,
        viewModel::onSaveClicked,
    )
}

@Composable
fun CartoonsSettingsDialog(
    state: CartoonsSettingsState,
    onAliveFirstCheckedChange: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
    onSaveClick: () -> Unit = {},
){
    Dialog(
        onDismissRequest = { onBack()}
    ){
        Column (
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(Spacing.medium)
        ){
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.titleMedium,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = state.aliveFirst,
                    onCheckedChange = { onAliveFirstCheckedChange(it) }
                )

                Spacer(Modifier.width(Spacing.medium))

                Text("Сначала Alive")
            }

            TextButton(
                onClick = onSaveClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartoonsSettingsDialogPreview() {
    CartoonsSettingsDialog(CartoonsSettingsState(true))
}