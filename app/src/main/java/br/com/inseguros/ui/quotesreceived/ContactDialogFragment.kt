package br.com.inseguros.ui.quotesreceived

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import br.com.inseguros.R
import br.com.inseguros.data.model.QuotationProposal

class ContactDialogFragment(private val quotationProposal: QuotationProposal) : DialogFragment() {

    private lateinit var companyDetailsIV: ImageView
    private lateinit var companyDetailsCompanyNameTV: TextView
    private lateinit var companyDetailsCompanyContactTV: TextView
    private lateinit var companyDetailsCallBtn: Button
    private lateinit var companyDetailsEmailBtn: Button
    private lateinit var companyDetailsSiteBtn: Button
    private lateinit var companyDetailsMapBtn: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view: View = requireActivity().layoutInflater.inflate(R.layout.company_contact_layout, null)

        companyDetailsIV = view.findViewById(R.id.companyDetailsIV)
        companyDetailsCompanyNameTV = view.findViewById(R.id.companyDetailsCompanyNameTV)
        companyDetailsCompanyContactTV = view.findViewById(R.id.companyDetailsCompanyContactTV)
        companyDetailsCallBtn = view.findViewById(R.id.companyDetailsCallBtn)
        companyDetailsEmailBtn = view.findViewById(R.id.companyDetailsEmailBtn)
        companyDetailsSiteBtn = view.findViewById(R.id.companyDetailsSiteBtn)
        companyDetailsMapBtn = view.findViewById(R.id.companyDetailsMapBtn)

        val alert = AlertDialog.Builder(requireActivity())
        alert.setView(view)

        val decodedString = Base64.decode(quotationProposal.companyIcon, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        companyDetailsIV.setImageBitmap(decodedByte)
        companyDetailsCompanyNameTV.text = quotationProposal.companyName
        companyDetailsCompanyContactTV.text =
            getString(R.string.company_details_contact, quotationProposal.contact)

        this.setupListeners()

        return alert.create()

    }

    private fun setupListeners() {

        companyDetailsCallBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {

                startActivity(
                    Intent(
                        Intent.ACTION_CALL, Uri
                            .parse("tel:${quotationProposal.contactPhone}")
                    )
                )
            }
        }

        companyDetailsEmailBtn.setOnClickListener {
            val recipient = quotationProposal.contactEmail
            val subject = "Proposta ${quotationProposal.vehicleModelNameAndFacYear}"
            val message = "Olá, gostaria de receber mais informações sobre a proposta " +
                    "${quotationProposal.vehicleModelNameAndFacYear}. Obrigado."
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            try {
                startActivity(Intent.createChooser(emailIntent, "Choose Email Client..."))
            }
            catch (e: Exception){
                "Erro ao tentar abrir cliente de email: ${e.message}"
            }
        }

        companyDetailsSiteBtn.setOnClickListener {
            val companySite = quotationProposal.companySite
            val url = if (companySite.startsWith("http://")) companySite
            else "http://${companySite}"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://${url}")))
        }

        companyDetailsMapBtn.setOnClickListener {
            val latLong = quotationProposal.companyLocation.split(",")
            if (latLong.size == 2) {
                val lat = latLong[0]
                val long = latLong[1]
                val mapsIntentUri = Uri.parse("geo:$lat,$long?z=18")
                val mapIntent = Intent(Intent.ACTION_VIEW, mapsIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
        }
    }

}