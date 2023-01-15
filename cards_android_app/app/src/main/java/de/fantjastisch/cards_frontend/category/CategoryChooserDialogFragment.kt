package de.fantjastisch.cards_frontend.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment

class CategoryChooserDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        TODO load Categories
        return ComposeView(requireContext()).apply { setContent { CategorySelect(categories = listOf()) } }
    }
}