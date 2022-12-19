package de.fantjastisch.cards.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsLayout(
    question: MutableState<String>,
    answer: MutableState<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Frage") },
            value = question.value,
            onValueChange = {
                question.value = it
            },
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Antwort") },
            value = answer.value,
            onValueChange = {
                answer.value = it
            },
        )
    }
}

@Preview
@Composable
fun QuestionsLayoutPreview() {
    MaterialTheme {
        QuestionsLayout(
            question = remember { mutableStateOf("") },
            answer = remember { mutableStateOf("") },
        )
    }
}