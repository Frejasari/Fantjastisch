@file:SuppressLint("CompositionLocalNaming")

package de.fantjastisch.cards_frontend.infrastructure

import android.annotation.SuppressLint
import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator

/**
 * Provider für die verschiedenen Navigatoren in der App.
 */
val FantMainNavigator = compositionLocalOf<Navigator> { error("MainNavigator not set") }
val FantTabNavigator = compositionLocalOf<TabNavigator> { error("TabNavigator not set") }
val FantBottomSheetNavigator =
    compositionLocalOf<BottomSheetNavigator> { error("BottomSheetNavigator not set") }
