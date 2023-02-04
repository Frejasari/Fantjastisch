package de.fantjastisch.cards_frontend.infrastructure

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf


@SuppressLint("CompositionLocalNaming")
val SnackbarProvider = compositionLocalOf<SnackbarHostState> { error("SnackbarProvider not set") }
