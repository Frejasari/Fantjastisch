package de.fantjastisch.cards_frontend.infrastructure

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.FadeTransition
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.GlossaryView
import de.fantjastisch.cards_frontend.category.CategoryGraphFragment
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
                // Der app Navigator, laesst sich ueberall in
                // Compose mit LocalNavigator.currentOrThrow ansprechen
                Navigator(screen = MainScreen()) {
                    // Rendert den derzeitigen Screen und die Uebergaenge
                    FadeTransition(navigator = it)
                }
            }
        }
    }
}


object LearningTab : Tab {
    @Composable
    override fun Content() {
        Text(
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center,
                text = "TODO! LearningOverviewFragment",
        )
    }

    override val options: TabOptions
        // object: = innere anonyme klasse erzeugen
        @Composable get() = TabOptions(
                index = 0u,
                title = stringResource(id = R.string.menu_learning_object_overview_label),
                icon = rememberVectorPainter(Icons.Default.TipsAndUpdates),
        )
}


object GlossaryTab : Tab {
    @Composable
    override fun Content() {
        GlossaryView()
    }

    override val options: TabOptions
        // object: = innere anonyme klasse erzeugen
        @Composable get() = TabOptions(
                index = 1u,
                title = stringResource(id = R.string.menu_glossar_label),
                icon = painterResource(id = R.drawable.ic_menu_glossar),
        )
}


object CategoriesTab : Tab {
    @Composable
    override fun Content() {
        CategoryGraphFragment()
    }

    override val options: TabOptions
        // object: = innere anonyme klasse erzeugen
        @Composable get() = TabOptions(
                index = 2u,
                title = stringResource(id = R.string.menu_categories_overview_label),
                icon = painterResource(id = R.drawable.ic_menu_categories),
        )
}