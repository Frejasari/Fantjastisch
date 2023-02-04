package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


/**
 * Zeigt 2 Texte übereinander an und darunter einen Divider.
 *
 * @author Freja Sender
 * **/
@Composable
fun TwoTextsWithDivider(headline: String, text: String) {
    Text(
        text = headline,
        fontWeight = FontWeight.Light
    )
    Text(
        text = text
    )

    Divider(
        modifier = Modifier
            .padding(vertical = 10.dp)
    )
}