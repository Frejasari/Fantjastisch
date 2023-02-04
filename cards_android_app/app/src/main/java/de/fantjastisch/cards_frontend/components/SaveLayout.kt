package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R

/**
 * Rendert einen Kontainer, welcher einen Button, sowie Scroll zur Verfügung stellt
 *
 * @param onSaveClicked Callback der auf dem Button Klick angewandt wird
 * @param modifier der Modifier dieser Komponente
 * @param content der Content innerhalb dieses Containers
 *
 * @author Freja Sender
 */
@Composable
fun SaveLayout(
    onSaveClicked: () -> Unit,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            content()
        }
        ElevatedButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = onSaveClicked
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }
    }
}