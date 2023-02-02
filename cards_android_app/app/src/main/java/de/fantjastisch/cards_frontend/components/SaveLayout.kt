package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R

//TODO USE!
@Composable
fun SaveLayout(
    onSaveClicked: () -> Unit,
    modifier: Modifier,
    buttonText: String = stringResource(R.string.save_button_text),
    content: @Composable ColumnScope.() -> Unit
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
            modifier = Modifier.fillMaxWidth(),
            onClick = onSaveClicked
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
fun LayoutWithSave(modifier: Modifier, onSaveClicked: () -> Unit, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
//            contentPadding = PaddingValues(all = 16.dp),
    ) {
        // Componente die ihre Kinder untereinander anzeigt.
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            content()
        }

        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onSaveClicked
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }
    }
}

