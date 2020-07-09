package br.com.inseguros.ui.quotes.genericscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.concrete.canarinho.validator.Validador
import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher
import br.com.inseguros.R
import br.com.inseguros.data.model.DatePickerModel
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.databinding.QuoteGenericScreenFragmentBinding
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.utils.DialogFragmentUtil
import br.com.inseguros.utils.validMaterialEditTextFilled
import com.rengwuxian.materialedittext.MaterialEditText
import java.util.*

class QuoteGenericScreenFragment : BaseFragment() {

    override val layout = R.layout.quote_generic_screen_fragment
    private lateinit var languages: Array<String>
    private lateinit var binding: QuoteGenericScreenFragmentBinding
    private lateinit var navController: NavController
    private val bornDatePickerModel = DatePickerModel()
    private val licenceDatePickerModel = DatePickerModel()

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
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.civilStateGenericSpn.adapter = arrayAdapter

        setupListeners()
        setupFieldsFormat()

    }

    private fun setupListeners() {

        //         this.bornDatePickerModel = DatePickerModel(year, month, day)
        //        this.licenceDatePickerModel = DatePickerModel(year, month, day)

        binding.quotesGenericToolbar.setNavigationOnClickListener {
            this.popBackStack()
        }

        binding.birthGenericIv.setOnClickListener {
            makeDataPickerDialog(binding.birthGenericMet, this.bornDatePickerModel)
        }

        // vehicle_licence_time_iv
        binding.vehicleLicenceTimeIv.setOnClickListener {
            makeDataPickerDialog(binding.vehicleLicenceTimeGenericMet, this.licenceDatePickerModel)
        }

        binding.cancelGenericBtn.setOnClickListener {
            confirmCancel()
        }

        binding.saveGenericBtn.setOnClickListener {
            when(binding.quoteGenericRg.checkedRadioButtonId) {
                R.id.male_generic_rb -> { Toast.makeText(requireContext(), "Masculino", Toast.LENGTH_SHORT).show() }
                R.id.female_generic_rb -> { Toast.makeText(requireContext(), "Feminino", Toast.LENGTH_SHORT).show() }
            }
            Toast.makeText(requireContext(), binding.civilStateGenericSpn.selectedItem.toString(), Toast.LENGTH_SHORT).show()
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
        binding.vehicleLicenceTimeGenericMet.addTextChangedListener(MascaraNumericaTextWatcher("##/##/####"))
    }

    @SuppressLint("SetTextI18n")
    private fun makeDataPickerDialog(met: MaterialEditText, datePickerModel: DatePickerModel) {

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, pYear, pMonth, pDayOfMonth ->
            // Display Selected date in TextView
            val dayStr = if (pDayOfMonth < 10) "0$pDayOfMonth" else pDayOfMonth.toString()
            val monthStr = if (pMonth+1 < 10) "0${pMonth+1}" else (pMonth+1).toString()
            met.setText("$dayStr$monthStr$pYear")
            datePickerModel.mYear = pYear
            datePickerModel.mMonth = pMonth
            datePickerModel.mDay = pDayOfMonth
            year = pYear
            month = pMonth
            day = pDayOfMonth
        }, year, month, day)
        dpd.show()

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