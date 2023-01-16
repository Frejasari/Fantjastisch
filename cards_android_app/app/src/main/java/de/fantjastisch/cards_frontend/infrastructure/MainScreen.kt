package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator

class MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Der naechsthoehere Navigator,
        // in diesem Fall der aus MainActivity

        val navigator = LocalNavigator.currentOrThrow
        TabNavigator(tab = mainScreenTabs.first()) { tabNavigator ->
            Scaffold(
                topBar = {
                    TopBar(
                        navigator = navigator,
                        tabNavigator = tabNavigator
                    )
                },
                bottomBar = {
                    NavigationBar {
                        mainScreenTabs.forEach {
                            NavigationBarItem(
                                selected = tabNavigator.current == it,
                                icon = {
                                    Icon(
                                        painter = it.options.icon!!,
                                        contentDescription = it.options.title
                                    )
                                },
                                label = { Text(text = it.options.title) },
                                onClick = { tabNavigator.current = it })
                        }
                    }
                }
            ) {
                Box(
                    modifier = Modifier.padding(it),
                ) {
                    CurrentTab()
                }
            }
        }
    }
}

private val mainScreenTabs = listOf(
    LearningTab,
    GlossaryTab,
    CategoriesTab,
)