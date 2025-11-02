package com.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.cartoons.presentation.screen.CartoonsDetailsDialog
import com.example.project.cartoons.presentation.screen.CartoonsListScreen
import com.example.project.cartoons.presentation.screen.CartoonsSettingsDialog
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

interface TopLevelRoute: Route {
    val icon: ImageVector
}
data object Characters: TopLevelRoute {
    override val icon = Icons.Default.Home
}

data object Cartoons: TopLevelRoute {
    override val icon = Icons.AutoMirrored.Filled.List
}

data class CartoonsDetails(val cartoons: CartoonsUiModel) : Route

data object CartoonsSettings : Route

@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)
    val dialogStrategy = remember { DialogSceneStrategy<Route>() }

    Scaffold(bottomBar = {
        NavigationBar {
            listOf(Characters, Cartoons).forEach { route ->
                NavigationBarItem(
                    icon = { Icon(route.icon, null) },
                    selected = topLevelBackStack.topLevelKey == route,
                    onClick = {
                        topLevelBackStack.addTopLevel(route)
                    }
                )
            }
        }
    }) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(padding),
            sceneStrategy = dialogStrategy,
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Characters> {
                    Text(
                        text = "Главная страница",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                entry<Cartoons> {
                    CartoonsListScreen(topLevelBackStack)
                }
                entry<CartoonsDetails>(
                    metadata = DialogSceneStrategy.dialog(DialogProperties())
                ) {
                    CartoonsDetailsDialog(it.cartoons)
                }
                entry<CartoonsSettings> (
                    metadata = DialogSceneStrategy.dialog(DialogProperties())
                ){
                    CartoonsSettingsDialog()
                }
            }
        )
    }
}