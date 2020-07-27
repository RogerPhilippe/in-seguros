package br.com.inseguros.ui.quotesreceived

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.inseguros.R
import br.com.inseguros.data.model.QuotationProposal
import kotlinx.android.synthetic.main.quote_received_list_item_layout.view.*

class QuoteReceivedAdapter(
    private val items: List<QuotationProposal>,
    private val parent: QuotesReceivedFragment
) : RecyclerView.Adapter<QuoteReceivedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {

            val decodedString = Base64.decode(companyIcon, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            holder.bindCompanyIcon(decodedByte)
            holder.bindCompanyName(companyName)
            holder.bindVehicleModelAndYear(vehicleModelNameAndFacYear)
            holder.bindCoverage(insuranceCoverage)
            holder.bindValue(proposalValue)

            holder.itemView.quote_received_contact_btn.setOnClickListener {
                parent.makeDetailsDialog(this)
            }

        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.quote_received_list_item_layout, parent, false)
    ) {

        fun bindCompanyIcon(img: Bitmap) = with(itemView) {
            quote_received_company_icon_iv.setImageBitmap(img)
        }
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
            quote_received_value_tv.text = resources.getString(R.string.currency_symbol, value)
        }

    }


}