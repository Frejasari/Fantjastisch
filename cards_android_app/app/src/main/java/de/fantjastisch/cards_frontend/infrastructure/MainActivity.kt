package de.fantjastisch.cards_frontend.infrastructure

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.FadeTransition
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.overview.CategoryOverviewView
import de.fantjastisch.cards_frontend.components.CustomSnackBar
import de.fantjastisch.cards_frontend.components.FantTopBar
import de.fantjastisch.cards_frontend.glossary.GlossaryView
import de.fantjastisch.cards_frontend.learning_overview.LearningOverviewView
import java.util.*

/**
 * App Theme, dass die Farben aus der Themes.xml lädt
 *
 * @param content der Content der App
 */
@Composable
fun CardsAppTheme(content: @Composable () -> Unit) {
    Mdc3Theme(content = content)
}

/**
 * Einstiegspunkt für die App, stellt einen Navigations- und Snackbar-Context bereit, in dem die App sich befindet.
 *
 * @author Freja Sender
 */
class MainActivity : AppCompatActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardsAppTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Box {
                    CompositionLocalProvider(
                        SnackbarProvider provides snackbarHostState,
                    ) {
                        TabNavigator(tab = mainScreenTabs.first()) { tabNavigator ->
                            BottomSheetNavigator(sheetShape = MaterialTheme.shapes.medium) { bottomSheetNavigator ->
                                // Der app Navigator, laesst sich ueberall verwenden
                                Navigator(screen = MainScreen()) { navigator ->
                                    CompositionLocalProvider(
                                        FantMainNavigator provides navigator,
                                        FantTabNavigator provides tabNavigator,
                                        FantBottomSheetNavigator provides bottomSheetNavigator,
                                    ) {
                                        // Rendert den derzeitigen Screen und die Uebergaenge
                                        FadeTransition(navigator = navigator)
                                    }
                                }
                            }
                        }
                        SnackbarHost(
                            modifier = Modifier.align(Alignment.TopCenter),
                            hostState = snackbarHostState
                        ) { snackbarData: SnackbarData ->
                            CustomSnackBar(snackbarData)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 1. Tab, der den [LearningOverviewView] anzeigt
 *
 * @author Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
object LearningTab : Tab {

    @Composable
    override fun Content() {
        Scaffold(topBar = { FantTopBar() }) {
            LearningOverviewView(
                modifier = Modifier.padding(it)
            )
        }
    }

    override val options: TabOptions
        // object: = innere anonyme klasse erzeugen
        @Composable get() = TabOptions(
            index = 0u,
            title = stringResource(id = R.string.menu_learning_object_overview_label),
            icon = rememberVectorPainter(Icons.Default.TipsAndUpdates),
        )
}

/**
 * 2. Tab, der den [GlossaryView] anzeigt
 *
 * @author Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
object GlossaryTab : Tab {
    @Composable
    override fun Content() {
        Scaffold(topBar = { FantTopBar() }) {
            GlossaryView(
                modifier = Modifier.padding(it)
            )
        }
    }

    override val options: TabOptions
        // object: = innere anonyme klasse erzeugen
        @Composable get() = TabOptions(
            index = 1u,
            title = stringResource(id = R.string.menu_glossar_label),
            icon = painterResource(id = R.drawable.ic_menu_glossar),
        )
}

/**
 * 2. Tab, der den [CategoryOverviewView] anzeigt
 *
 * @author Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
object CategoriesTab : Tab {
    @Composable
    override fun Content() {
        Scaffold(topBar = { FantTopBar() }) {
            CategoryOverviewView(
                modifier = Modifier.padding(it)
            )
        }
    }

    override val options: TabOptions
        // object: = innere anonyme klasse erzeugen
        @Composable get() = TabOptions(
            index = 2u,
            title = stringResource(id = R.string.menu_categories_overview_label),
            icon = painterResource(id = R.drawable.ic_menu_categories),
        )
}