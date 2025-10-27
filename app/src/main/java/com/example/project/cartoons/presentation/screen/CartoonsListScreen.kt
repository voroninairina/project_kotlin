package com.example.project.cartoons.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.project.Cartoons
import com.example.project.CartoonsDetails
import com.example.project.cartoons.presentation.MockData
import com.example.project.cartoons.presentation.model.CartoonsListFilter
import com.example.project.cartoons.presentation.model.CartoonsListViewState
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.cartoons.presentation.viewModel.CartoonsListViewModel
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import com.example.project.uikit.FullscreenError
import com.example.project.uikit.FullscreenLoading
import com.example.project.uikit.Spacing
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun CartoonsListScreen(topLevelBackStack: TopLevelBackStack<Route>) {
    val viewModel = koinViewModel<CartoonsListViewModel>()
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    CartoonsListScreenContent(
        state,
        viewModel::onCartoonsClick,
        viewModel::onRetryClick,
        viewModel::onSettingsClick,
        viewModel::onFilterChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartoonsListScreenContent(
    state: CartoonsListViewState,
    onCartoonsClick: (CartoonsUiModel) -> Unit = {},
    onRetryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onFilterChange: (CartoonsListFilter) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {onSettingsClick()}) {
                Icon(Icons.Default.Settings, "Settings" )
            }
        },
        topBar = {
            Column {
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

                TopAppBar(
                    { CartoonsListFilters(state, onFilterChange) },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ){
        Box(Modifier.padding(it)) {
            when (state.listState) {
                CartoonsListViewState.State.Loading -> {
                    FullscreenLoading()
                }

                is CartoonsListViewState.State.Error -> {
                    FullscreenError(
                        retry = { onRetryClick() },
                        text = state.listState.error
                    )
                }

                is CartoonsListViewState.State.Success -> {
                    LazyColumn {
                        state.listState.data.forEach { cartoons ->
                            item {
                                CartoonsListItem(cartoons) { onCartoonsClick(it) }
                            }
                        }
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

@Composable
private fun CartoonsListFilters (
    state: CartoonsListViewState,
    onFilterChange: (CartoonsListFilter) -> Unit,
) {
    FlowRow (
        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        state.filters.forEach { filter ->
            FilterChip(
                selected = filter == state.currentFilter,
                label = { Text(filter.text)},
                onClick = { onFilterChange(filter)},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartoonsListPreview() {
    CartoonsListScreenContent(
        CartoonsListViewState(CartoonsListViewState.State.Success(MockData.getCartoons()))
    )
}
