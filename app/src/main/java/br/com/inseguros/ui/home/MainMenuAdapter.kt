package br.com.inseguros.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import br.com.inseguros.R
import br.com.inseguros.data.model.MainMenu
import kotlinx.android.synthetic.main.main_menu_item.view.*
import org.jetbrains.annotations.NotNull

class MainMenuAdapter(
    @NotNull private val items: MutableList<MainMenu>,
    @NotNull private val parent: HomeFragment
) : RecyclerView.Adapter<MainMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {

            val decodedString = Base64.decode(this.menuIcon, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            holder.bindIcon(decodedByte)
            holder.bindLabel(this.description)

            holder.itemView.setOnClickListener {
                when(this.id) {
                    "main_menu_id_item_1" -> parent.navControllerNavigateTo(
                        R.id.action_navigation_home_to_quoteFragment, this.description
                    )
                    "main_menu_id_item_2" -> parent.navControllerNavigateTo(
                        R.id.action_navigation_home_to_historicFragment, this.description
                    )
                    "main_menu_id_item_3" -> parent.navControllerNavigateTo(
                        R.id.action_navigation_home_to_quotesReceivedFragment, this.description
                    )
                    "main_menu_id_item_4" -> parent.navControllerNavigateTo(
                        R.id.action_navigation_home_to_messagesFragment, this.description
                    )
                }
            }

        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.main_menu_item, parent, false)
    ) {

        fun bindIcon(img: Bitmap) = with(itemView) { itemView.mainMenuIconIV.setImageBitmap(img) }
        fun bindLabel(label: String) = with(itemView) { itemView.mainMenuLabelTV.text = label }

    }

}
