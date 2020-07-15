package br.com.inseguros.ui.quotes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.in_seguros_utils.makeShortToast
import br.com.inseguros.R
import br.com.inseguros.data.model.MainSubMenu
import kotlinx.android.synthetic.main.main_sub_manu_item.view.*
import org.jetbrains.annotations.NotNull

class QuoteAdapter(
    @NotNull private val items: MutableList<MainSubMenu>,
    @NotNull private val parent: QuoteFragment
) : RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {
            val decodedString = Base64.decode(this.menuIcon, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            holder.bindIcon(decodedByte)
            holder.bindTitle(this.title)
            holder.bindSubTitle(this.subTitle)

            holder.itemView.setOnClickListener {
                when(this.id) {
                    "main_menu_id_item_1" -> parent.navControllerNavigateTo(
                        R.id.action_quoteFragment_to_quoteGenericScreenFragment, this
                    )
                    "main_menu_id_item_2" -> parent.navControllerNavigateTo(
                        R.id.action_quoteFragment_to_quoteGenericScreenFragment, this
                    )
                    "main_menu_id_item_3" -> {
                        parent.getString(R.string.not_implemented_in_alpha_yet).makeShortToast(parent.requireContext())
                        /**
                         * parent.navControllerNavigateTo(R.id.action_quoteFragment_to_quoteHouseFragment, this)
                         */
                    }
                    "main_menu_id_item_4" -> {
                        parent.getString(R.string.not_implemented_in_alpha_yet).makeShortToast(parent.requireContext())
                        /**
                         * parent.navControllerNavigateTo(R.id.action_quoteFragment_to_quoteLifeFragment, this)
                         */
                    }
                }
            }

        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.main_sub_manu_item, parent, false)
    ) {
        fun bindIcon(img: Bitmap) = with(itemView) { itemView.mainSubMenuIconIV.setImageBitmap(img) }
        fun bindTitle(title: String) = with(itemView) { itemView.mainSubMenuTitleTV.text = title }
        fun bindSubTitle(subTitle: String) = with(itemView) { itemView.mainSubMenuSubtitleTV.text = subTitle }
    }

}