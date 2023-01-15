package de.fantjastisch.cards_frontend.category

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.fantjastisch.cards.R
import java.util.*
import kotlin.properties.Delegates

// UI Klasse ,die einen recycler view verwendet, um eine Liste mit selektierbaren Items anzuzeigen.
class MultiSelectionList @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
) : RecyclerView(context, attrs, defStyle) {

    data class Item(val label: String, val id: UUID, var isChecked: Boolean)

    private var onItemSelectedFunction: (item: Item) -> Unit = {}

    // var -> bekommt automatisch getter und setter, weil nicht private
    // Delegates.ovservable(initialer Wert, Funktion die ausgeführt wird, nachdem die var neu gesetzt wurde)
    var items: List<Item> by Delegates.observable(emptyList()) { _, _, _ ->
        adapter.notifyDataSetChanged()
    }

    fun onItemSelected(function: (item: Item) -> Unit) {
        this.onItemSelectedFunction = function
    }

    // ViewHolder: verbindet die Items mit dem eigentlichen View
    // da Views sehr teuer sind, recyclen wir die views.
    private class SelectionItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val label = itemView.findViewById<TextView>(R.id.multiselect_label_text)
        val checkbox = itemView.findViewById<CheckBox>(R.id.multiselect_checkbox)
        fun bind(item: Item) {
            label.text = item.label
            checkbox.isChecked = item.isChecked
            checkbox.setOnCheckedChangeListener { _, isChecked -> item.isChecked = isChecked }
        }
    }

    //
    private val adapter = object : Adapter<SelectionItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionItemViewHolder {
            return SelectionItemViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.multiselect_recycler_view_row, parent, false)
            )
        }

        override fun onBindViewHolder(holder: SelectionItemViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }

    }

    init {
        setAdapter(adapter)
    }

}