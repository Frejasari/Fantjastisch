package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.TabNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigator: Navigator,
    tabNavigator: TabNavigator,
) {
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.
    val isContextMenuOpen = remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = { Text(text = tabNavigator.current.options.title) },
        actions = {

            AnimatedVisibility(visible = tabNavigator.current == GlossaryTab) {
//                TODO
                IconButton(
                    onClick = { }
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "more")
                }
            }

            TobBarCreateMenu(navigator = navigator)
        }
    )
}
