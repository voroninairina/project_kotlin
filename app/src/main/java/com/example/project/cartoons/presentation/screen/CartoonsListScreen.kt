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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project.Cartoons
import com.example.project.CartoonsDetails
import com.example.project.cartoons.presentation.MockData
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack


@Composable
fun CartoonsListScreen(topLevelBackStack: TopLevelBackStack<Route>) {
    val cartoons = remember { MockData.getCartoons() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Заголовок с фоном и отступом
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Персонажи мультсериала Rick and Morty",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.surfaceBright,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Список персонажей
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f) // чтобы занимал все доступное пространство
        ) {
            items(
                items = cartoons,
                key = { it.id }
            ) { cartoon ->
                CartoonsListItem(cartoon) {
                    topLevelBackStack.add(CartoonsDetails(it))
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
            text = cartoons.id,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = cartoons.name,
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = cartoons.status,
            style = MaterialTheme.typography.bodyMedium,
        )

        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun CartoonsListPreview() {
    CartoonsListScreen(TopLevelBackStack<Route>(Cartoons))
}