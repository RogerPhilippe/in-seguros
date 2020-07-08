package br.com.inseguros.ui.quotes.genericscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.concrete.canarinho.validator.Validador
import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher
import br.com.inseguros.R
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.databinding.QuoteGenericScreenFragmentBinding
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.ui.quotes.genericscreen.utils.VehicleLicenceTimeDialogYearTextChanged
import br.com.inseguros.utils.DialogFragmentUtil
import br.com.inseguros.utils.makeShortToast
import br.com.inseguros.utils.validMaterialEditTextFilled
import com.rengwuxian.materialedittext.MaterialEditText
import java.util.*

class QuoteGenericScreenFragment : BaseFragment() {

    override val layout = R.layout.quote_generic_screen_fragment
    private lateinit var genreRadioBox: String
    private lateinit var languages: Array<String>
    private lateinit var binding: QuoteGenericScreenFragmentBinding
    private lateinit var navController: NavController
    private lateinit var monthMed: MaterialEditText
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuoteGenericScreenFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        val mainSubMenu = arguments?.get("main_sub_menu") as MainSubMenu
        binding.quotesGenericToolbar.title = mainSubMenu.title

        val decodedString = Base64.decode(mainSubMenu.menuIcon, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.quoteGenericIconIv.setImageBitmap(decodedByte)
        binding.quoteGenericDescriptionTv.text = mainSubMenu.description

        languages = arrayOf(getString(R.string.civil_state_option_single), getString(R.string.civil_state_option_married))
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, languages)
        binding.civilStateGenericSpn.adapter = arrayAdapter

        setupListeners()
        setupFieldsFormat()
        fillDateVariables()

    }

    private fun setupListeners() {

        binding.quotesGenericToolbar.setNavigationOnClickListener {
            this.popBackStack()
        }

        binding.civilStateGenericSpn.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), getString(R.string.select_option), Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genreRadioBox = languages[position]
            }

        }

        binding.birthGenericIv.setOnClickListener { makeDataPickerDialog() }

        // vehicle_licence_time_iv
        binding.vehicleLicenceTimeIv.setOnClickListener { makeMonthCalculator() }

        binding.cancelGenericBtn.setOnClickListener {
            confirmCancel()
        }

        binding.saveGenericBtn.setOnClickListener {
            when(binding.quoteGenericRg.checkedRadioButtonId) {
                R.id.male_generic_rb -> { Toast.makeText(requireContext(), "Masculino", Toast.LENGTH_SHORT).show() }
                R.id.female_generic_rb -> { Toast.makeText(requireContext(), "Feminino", Toast.LENGTH_SHORT).show() }
            }
            validateFields()
            validateCPF()
        }

    }

    private fun setupFieldsFormat() {

        // CPF
        binding.cpfGenericMet
            .addTextChangedListener(MascaraNumericaTextWatcher("###.###.###-##"))
        // CEP
        binding.vehicleOvernightZipGenericMet
            .addTextChangedListener(MascaraNumericaTextWatcher("#####-###"))
        // Data
        binding.birthGenericMet.addTextChangedListener(MascaraNumericaTextWatcher("##/##/####"))
    }

    private fun fillDateVariables() {

        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
    }

    @SuppressLint("SetTextI18n")
    private fun makeDataPickerDialog() {

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            // Display Selected date in TextView
            val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
            val monthStr = if (month+1 < 10) "0${month+1}" else (month+1).toString()
            binding.birthGenericMet.setText("$dayStr$monthStr$year")
            mYear = year
            mMonth = month
            mDay = dayOfMonth
        }, mYear, mMonth, mDay)
        dpd.show()

    }

    private fun makeMonthCalculator() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.vehicle_licence_time_calculator_dialog)
        val yearMed = dialog.findViewById<MaterialEditText>(R.id.vehicle_licence_time_dialog_year_met)
        monthMed = dialog.findViewById(R.id.vehicle_licence_time_dialog_month_met)
        val applyBtn = dialog.findViewById<Button>(R.id.vehicle_licence_time_dialog_apply_btn)
        yearMed.addTextChangedListener(VehicleLicenceTimeDialogYearTextChanged(monthMed))
        applyBtn.setOnClickListener {
            if (monthMed.text.isNotEmpty() && monthMed.text.toString().toInt() > 0) {
                binding.vehicleLicenceTimeGenericMet.text = monthMed.text
                dialog.dismiss()
            } else { "Preencha o ano.".makeShortToast(requireContext()) }
        }
        dialog.show()
    }

    private fun validateFields(): Boolean {

        val errors = arrayListOf<Boolean>()

        if (!validMaterialEditTextFilled(binding.fullNameGenericMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.cpfGenericMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.birthGenericMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleLicenceNumberGenericMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleLicenceTimeGenericMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleOvernightZipGenericMet, requireContext()))
            errors.add(true)

        return errors.isEmpty()

    }

    private fun validateCPF(): Boolean {

        var status = true

        if (binding.cpfGenericMet.text.isEmpty() &&
            !Validador.CPF.ehValido(binding.cpfGenericMet.text.toString())) {

            binding.cpfGenericMet.error = "Erro no formato do CPF"
            status = false
        }

        return status
    }

    private fun confirmCancel() {

        val filledFields = arrayListOf<Boolean>()

        if (binding.fullNameGenericMet.text.isNotEmpty())
            filledFields.add(true)
        if (binding.cpfGenericMet.text.isNotEmpty())
            filledFields.add(true)
        if (binding.birthGenericMet.text.isNotEmpty())
            filledFields.add(true)
        if (binding.vehicleLicenceNumberGenericMet.text.isNotEmpty())
            filledFields.add(true)
        if (binding.vehicleLicenceTimeGenericMet.text.isNotEmpty())
            filledFields.add(true)
        if (binding.vehicleOvernightZipGenericMet.text.isNotEmpty())
            filledFields.add(true)

        if (filledFields.isNotEmpty())
            DialogFragmentUtil(
                getString(R.string.exit_confirmation_discard_changes),
                { this.popBackStack() },
                {}
            ).show(parentFragmentManager, "quote_generic_dialog_fragment")
        else
            this.popBackStack()

    }

    private fun popBackStack() = navController.popBackStack()

}