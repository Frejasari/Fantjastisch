package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator

class MainScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Der naechsthoehere Navigator,
        // in diesem Fall der aus MainActivity

        val navigator = FantMainNavigator.current
        TabNavigator(tab = mainScreenTabs.first()) { tabNavigator ->
            BottomSheetNavigator (sheetShape = MaterialTheme.shapes.medium) { bottomSheetNavigator ->
                CompositionLocalProvider(
                    FantTabNavigator provides tabNavigator,
                    FantBottomSheetNavigator provides bottomSheetNavigator
                ) {
                    Scaffold(
                        topBar = {
                            TopBar()
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
    }
}

private val mainScreenTabs = listOf(
    LearningTab,
    GlossaryTab,
    CategoriesTab
)