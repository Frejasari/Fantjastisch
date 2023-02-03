@file:SuppressLint("CompositionLocalNaming")

package de.fantjastisch.cards_frontend.infrastructure

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


val SnackbarProvider = compositionLocalOf<SnackbarHostState> { error("SnackbarProvider not set") }

@Composable
fun CustomSnackBar(snackbarData: SnackbarData) {
    Box(
        modifier = Modifier.padding(24.dp)
    ) {
        Surface(
            color = Color.Red,
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 6.dp,
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Outlined.ErrorOutline, contentDescription = "error")
                Text(
                    text = snackbarData.visuals.message
                )
            }
        }
    }
}
