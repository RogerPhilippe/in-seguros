package br.com.inseguros.ui.quotesreceived

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.inseguros.R
import br.com.inseguros.data.model.QuotationProposal
import kotlinx.android.synthetic.main.quote_received_list_item_layout.view.*

class QuoteReceivedAdapter(
    private val items: List<QuotationProposal>
) : RecyclerView.Adapter<QuoteReceivedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {
            holder.bindCompanyName(companyName)
            holder.bindVehicleModelAndYear(vehicleModelNameAndFacYear)
            holder.bindValue(proposalValue)
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.quote_received_list_item_layout, parent, false)
    ) {

        fun bindCompanyName(companyName: String) = with(itemView) {
            quote_received_company_name_tv.text = companyName
        }
        fun bindVehicleModelAndYear(vehicleModelAndYear: String) = with(itemView) {
            quote_received_vehicle_tv.text = vehicleModelAndYear
        }
        fun bindCoverage(coverage: String) = with(itemView) {
            quote_received_coverage_tv.text = coverage
        }
        fun bindValue(value: String) = with(itemView) {
            quote_received_value_tv.text = "R$ $value"
        }

        /**
         *  quote_received_company_icon_iv
         *
         */

    }


}