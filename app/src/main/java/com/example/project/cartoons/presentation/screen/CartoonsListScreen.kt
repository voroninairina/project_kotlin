package com.example.project.cartoons.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.project.Cartoons
import com.example.project.CartoonsDetails
import com.example.project.cartoons.presentation.MockData
import com.example.project.cartoons.presentation.model.CartoonsListViewState
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.cartoons.presentation.viewModel.CartoonsListViewModel
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import com.example.project.uikit.FullscreenError
import com.example.project.uikit.FullscreenLoading
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun CartoonsListScreen(topLevelBackStack: TopLevelBackStack<Route>) {
    val viewModel = koinViewModel<CartoonsListViewModel>()
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    CartoonsListScreenContent(
        state.state,
        viewModel::onCartoonsClick,
        viewModel::onRetryClick,
    )
}

@Composable
private fun CartoonsListScreenContent(
    state: CartoonsListViewState.State,
    onCartoonsClick: (CartoonsUiModel) -> Unit = {},
    onRetryClick: () -> Unit = {},
) {
    when (state) {
        CartoonsListViewState.State.Loading -> {
            FullscreenLoading()
        }

        is CartoonsListViewState.State.Error -> {
            FullscreenError(
                retry = { onRetryClick() },
                text = state.error
            )
        }

        is CartoonsListViewState.State.Success -> {
            LazyColumn {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "Персонажи мультсериала Rick and Morty",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.surfaceBright,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                state.data.forEach { cartoons ->
                    item {
                        CartoonsListItem(cartoons) { onCartoonsClick(it) }
                    }
                }
            }
        }
    }
}

@Composable
fun CartoonsListItem(cartoons: CartoonsUiModel, onCartoonsClick: (CartoonsUiModel) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onCartoonsClick(cartoons) }
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {


        Text(
            text = cartoons.name,
            style = MaterialTheme.typography.titleMedium,
        )

        cartoons.status?.let {
            Text(
                text = "Статус: ${it}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        cartoons.location?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium,
            )
        }

        cartoons.firstSeenIn?.let {
            Text(
                text = "Первый раз заметили: ${it}",
                style = MaterialTheme.typography.labelMedium,
            )
        }

        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun CartoonsListPreview() {
    CartoonsListScreenContent(
        CartoonsListViewState.State.Success(MockData.getCartoons())
    )
}