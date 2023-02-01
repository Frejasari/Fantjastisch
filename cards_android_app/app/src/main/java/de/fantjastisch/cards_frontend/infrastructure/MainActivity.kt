package de.fantjastisch.cards_frontend.infrastructure

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import de.fantjastisch.cards_frontend.category.cat_glossary.CatGlossaryView
import de.fantjastisch.cards_frontend.glossary.GlossaryView
import de.fantjastisch.cards_frontend.learning_overview.LearningOverviewScreen
import java.util.*

@Composable
fun CardsAppTheme(content: @Composable () -> Unit) {
    Mdc3Theme(content = content)
}


class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardsAppTheme {
                TabNavigator(tab = mainScreenTabs.first()) { tabNavigator ->
                    BottomSheetNavigator (sheetShape = MaterialTheme.shapes.medium) { bottomSheetNavigator ->
                        // Der app Navigator, laesst sich ueberall in
                        // Compose mit LocalNavigator.currentOrThrow ansprechen
                        Navigator(screen = MainScreen()) { navigator ->
                            CompositionLocalProvider(
                                FantMainNavigator provides navigator,
                                FantTabNavigator provides tabNavigator,
                                FantBottomSheetNavigator provides bottomSheetNavigator
                            ) {
                                // Rendert den derzeitigen Screen und die Uebergaenge
                                FadeTransition(navigator = navigator)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
object LearningTab : Tab {
    @Composable
    override fun Content() {
        Navigator(LearningOverviewScreen()) {
            FadeTransition(navigator = it)
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

@OptIn(ExperimentalMaterial3Api::class)

object CategoriesTab : Tab {
    @Composable
    override fun Content() {
        Scaffold(topBar = { FantTopBar() }) {
            CatGlossaryView(
                modifier = Modifier.padding(it)
            )
        }
    }

    override val options: TabOptions
        // object: = innere anonyme klasse erzeugen
        @Composable get() = TabOptions(
            index = 2u,
            title = stringResource(id = R.string.menu_categories_overview_label),
            icon = painterResource(id = R.drawable.topic),
        )
}