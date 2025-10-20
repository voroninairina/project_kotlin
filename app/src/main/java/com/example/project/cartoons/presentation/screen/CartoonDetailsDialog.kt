package com.example.project.cartoons.presentation.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.project.cartoons.presentation.MockData
import com.example.project.cartoons.presentation.model.CartoonsDetailsViewState
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.cartoons.presentation.viewModel.CartoonsDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import com.example.project.uikit.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartoonsDetailsDialog(
    cartoons: CartoonsUiModel,
) {
    val viewModel = koinViewModel<CartoonsDetailsViewModel> {
        parametersOf(cartoons)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = { viewModel.onBack() },
    ) {
        CartoonsDetailsContent(state, viewModel::onRatingChanged)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartoonsDetailsContent(
    state: CartoonsDetailsViewState,
    onRatingChanged: (Float) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val context = LocalContext.current

        if (!state.cartoons.imageUrl.isNullOrBlank()) {
            GlideImage(
                model = state.cartoons.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(120.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }

        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            modifier = Modifier.clickable {
                shareText(context, state.cartoons.name)
            }
        )

        state.cartoons.status?.let {
            Text(
                text = "Статус: ${it}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = state.cartoons.name,
            style = MaterialTheme.typography.titleMedium,
        )

        if (!state.cartoons.location.isNullOrBlank()) {
            Text(
                text = state.cartoons.location,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        RatingBar(state.rating) { onRatingChanged(it) }

        if (state.userVoteVisible) {
            Text("Ваша оценка: ${state.rating}")
        }
    }
}

fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Поделиться через"))
}

@Preview(showBackground = true)
@Composable
fun CartoonsDetailDialogPreview() {
    CartoonsDetailsContent(
        CartoonsDetailsViewState(MockData.getCartoons().first()),
    )
}