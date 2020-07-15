package br.com.inseguros.ui.historic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.inseguros.R
import br.com.inseguros.data.enums.QuoteTypeEnum
import br.com.inseguros.data.model.QuoteVehicle
import br.com.in_seguros_utils.convertDateToString
import kotlinx.android.synthetic.main.historic_item_layout.view.*
import java.util.*

class HistoricAdapter(
    private val items: MutableList<QuoteVehicle>,
    private val parent: HistoricFragment
): RecyclerView.Adapter<HistoricAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {

            holder.bindDateQuote(convertDateToString(Date(this.quoteDate)))
            holder.bindDescription("Cotação ${this.vehicleType}")
            holder.bindVehicleQuoteModel("${this.vehicleModel} ${this.vehicleModelYear}")
            holder.bindVehicleRegisterNum(this.vehicleRegisterNum)
            val quoteStatus = when(this.quoteStatus) {
                QuoteTypeEnum.UNDER_ANALYSIS.value -> parent.getString(R.string.under_analysis_quote)
                QuoteTypeEnum.CANCELED.value -> parent.getString(R.string.canceled_quote_status_label)
                QuoteTypeEnum.APPROVED.value -> parent.getString(R.string.approved_quote_status_label)
                QuoteTypeEnum.FINISHED.value -> parent.getString(R.string.finished_quote_status_label)
                else -> "UNKNOWN"
            }
            holder.bindVehicleQuoteStatus(quoteStatus)

            if (this.quoteStatus != QuoteTypeEnum.CANCELED.value) {

                holder.itemView.historicEditBtn.setOnClickListener { parent.editQuote(this) }
                holder.itemView.historicCancelBtn.setOnClickListener { parent.cancelQuote(this) }
                holder.itemView.historicEditBtn.isEnabled = true
                holder.itemView.historicCancelBtn.isEnabled = true
            } else {
                holder.itemView.historicEditBtn.isEnabled = false
                holder.itemView.historicCancelBtn.isEnabled = false
            }

        }
    }

    fun removeAt(position: Int) {
        val itemToRemove = items[position]
        items.removeAt(position)
        notifyItemRemoved(position)
        parent.cancelItemDeletion(position, itemToRemove)
    }

    fun undoItemRemoved(position: Int, item: QuoteVehicle) {
        items.add(position, item)
        notifyDataSetChanged()
    }

    class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.historic_item_layout, parent, false)
    ) {
        fun bindDateQuote(dateStr: String) = with(itemView) { historicDateQuoteTV.text = dateStr }
        fun bindDescription(description: String) = with(itemView) { historicDescriptionQuoteTV.text = description }
        fun bindVehicleQuoteModel(model: String) = with(itemView) { historicVehicleQuoteModelYearTV.text = model }
        fun bindVehicleRegisterNum(registerNum: String) = with(itemView) { historicVehicleRegisterNumTV.text = registerNum }
        fun bindVehicleQuoteStatus(quoteStatus: String) = with(itemView) { historicQuoteStatusTV.text = quoteStatus }
    }

}