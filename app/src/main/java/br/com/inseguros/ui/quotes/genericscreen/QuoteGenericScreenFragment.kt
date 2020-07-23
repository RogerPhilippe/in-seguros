package br.com.inseguros.ui.quotes.genericscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.concrete.canarinho.validator.Validador
import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher
import br.com.in_seguros_utils.convertDateToLong
import br.com.in_seguros_utils.convertDateToString
import br.com.in_seguros_utils.makeErrorShortToast
import br.com.in_seguros_utils.makeShortToast
import br.com.inseguros.R
import br.com.inseguros.data.sessions.UserSession
import br.com.inseguros.data.enums.CivilStateEnum
import br.com.inseguros.data.enums.QuoteTypeEnum
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.enums.VehicleTypeEnum
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.databinding.QuoteGenericScreenFragmentBinding
import br.com.inseguros.events.RefreshHistoricListEvent
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.utils.DialogFragmentUtil
import br.com.inseguros.utils.validMaterialEditTextFilled
import com.rengwuxian.materialedittext.MaterialEditText
import org.greenrobot.eventbus.EventBus
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class QuoteGenericScreenFragment : BaseFragment() {

    override val layout = R.layout.quote_generic_screen_fragment
    private lateinit var languages: Array<String>
    private lateinit var binding: QuoteGenericScreenFragmentBinding
    private lateinit var navController: NavController
    private var vehicleType: String? = null
    private var editMode = false
    private lateinit var quoteVehicleItemToEdit: QuoteVehicle
    private lateinit var brandsArrayAdapter: ArrayAdapter<String>
    private val mViewModel: QuoteGenericScreenViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuoteGenericScreenFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        val mainSubMenu = arguments?.get("main_sub_menu") as MainSubMenu
        binding.quotesGenericToolbar.title = mainSubMenu.title

        val quoteItem = arguments?.get("quote_vehicle") as QuoteVehicle?
        editMode = quoteItem != null

        if (editMode) {
            binding.saveGenericBtn.text = getString(R.string.update_label)
            quoteVehicleItemToEdit = quoteItem!!
            binding.headerContainer.visibility = View.GONE
            vehicleType = quoteItem.vehicleType
        }

        if (mainSubMenu.menuIcon.isNotEmpty()) {
            val decodedString = Base64.decode(mainSubMenu.menuIcon, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            binding.quoteGenericIconIv.setImageBitmap(decodedByte)
        }

        if (mainSubMenu.description.isNotEmpty())
            binding.quoteGenericDescriptionTv.text = mainSubMenu.description

        languages = arrayOf(getString(R.string.civil_state_option_single), getString(R.string.civil_state_option_married))
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.civilStateGenericSpn.adapter = arrayAdapter

        if (vehicleType == null) {
            vehicleType = when {
                mainSubMenu.title.contains(VehicleTypeEnum.CAR.value) -> VehicleTypeEnum.CAR.value
                mainSubMenu.title.contains(VehicleTypeEnum.MOTORCYCLE.value) -> VehicleTypeEnum.MOTORCYCLE.value
                else -> "UNKNOWN"
            }
        }

        // Temporary vehicle brand spinner fill
        val vehicleBrands = when(vehicleType) {
            VehicleTypeEnum.CAR.value ->
                arrayListOf("GM Chevrolet", "Ford", "Fiat", "Mercedes", "BMW")
            VehicleTypeEnum.MOTORCYCLE.value ->
                arrayListOf("Yamaha", "Honda", "Harley-Davidson", "Kawazaki", "BMW")
            else -> arrayListOf()
        }

        brandsArrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vehicleBrands)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.vehicleBrandGenericSpn.adapter = brandsArrayAdapter

        setupListeners()
        setupFieldsFormat()
        setupObservers()

        if (editMode)
            this.fillFieldsInEditMode()

    }

    override fun onResume() {
        super.onResume()
        trackEvent("quote_generic_fragment", "onResume")
        trackEvent("vehicle_type", (vehicleType as String))
        trackEvent("editMode", editMode)
    }

    private fun setupListeners() {

        binding.quotesGenericToolbar.setNavigationOnClickListener {
            this.popBackStack()
        }

        binding.birthGenericIv.setOnClickListener {
            makeDataPickerDialog(binding.birthGenericMet)
        }

        binding.vehicleLicenceTimeIv.setOnClickListener {
            makeDataPickerDialog(binding.vehicleLicenceTimeGenericMet)
        }

        binding.cancelGenericBtn.setOnClickListener {
            confirmCancel()
        }

        binding.saveGenericBtn.setOnClickListener {
            showContent(false)
            persistRegister()
        }

    }

    private fun persistRegister() {

        val genreChar = when(binding.quoteGenericRg.checkedRadioButtonId) {
            R.id.male_generic_rb -> "M"
            R.id.female_generic_rb -> "F"
            else -> ""
        }
        val civilState = getCivilStateSelected()
        val vehicleBrand = binding.vehicleBrandGenericSpn.selectedItem?.toString()
        if (genreChar.isEmpty()) {
            "Selecione o genero".makeErrorShortToast(requireContext())
        } else if (civilState.isEmpty()) {
            "Selecione o estado civíl".makeErrorShortToast(requireContext())
        } else if (vehicleBrand.isNullOrEmpty()) {
            "Selecione a marca do veículo".makeErrorShortToast(requireContext())
        } else if (!validateFields()) {
            "Preencha todos os campos obrigatórios".makeErrorShortToast(requireContext())
        } else if (!validateCPF()) {
            "CPF inválido".makeErrorShortToast(requireContext())
        } else {

            mViewModel.getCurrentQuoteVehicleLiveData().value.apply {
                this?.id = if (editMode) quoteVehicleItemToEdit.id else 0L
                this?.userID = UserSession.getUserID()
                this?.fullName = binding.fullNameGenericMet.text.toString()
                this?.cpf = binding.cpfGenericMet.text.toString()
                this?.birthDate = convertDateToLong(binding.birthGenericMet.text.toString())
                this?.genre = genreChar
                this?.civilState = civilState
                this?.vehicleType = vehicleType!!
                this?.vehicleBrand = vehicleBrand
                this?.vehicleModel = binding.vehicleModelNameGenericMet.text.toString()
                this?.vehicleYearManufacture = binding.vehicleYeaManufacturerMet.text.toString()
                this?.vehicleModelYear = binding.vehicleModelYearMet.text.toString()
                this?.vehicleLicenceNumber = binding.vehicleLicenceNumberGenericMet.text.toString()
                this?.vehicleLicenceTime = convertDateToLong(binding.vehicleLicenceTimeGenericMet.text.toString())
                this?.overnightCEP = binding.vehicleOvernightZipGenericMet.text.toString()
                this?.quoteDate = Calendar.getInstance().timeInMillis
                this?.quoteStatus = QuoteTypeEnum.UNDER_ANALYSIS.value
                this?.vehicleRegisterNum = binding.vehicleRegisterNumMet.text.toString()
                this?.status = true
            }

            mViewModel.insertQuoteVehicle()

        }
    }

    private fun getCivilStateSelected(): String {
        return when(binding.civilStateGenericSpn.selectedItemId.toInt()) {
            1 -> CivilStateEnum.SINGLE.value
            2 -> CivilStateEnum.MARRIED.value
            else -> ""
        }
    }

    private fun fillFieldsInEditMode() {

        with(quoteVehicleItemToEdit) {
            binding.fullNameGenericMet.setText(fullName)
            binding.cpfGenericMet.setText(cpf)
            binding.birthGenericMet.setText(convertDateToString(Date(birthDate)))
            setRadioBox(genre)
            setCivilStateSpn(civilState)
            binding.vehicleModelNameGenericMet.setText(vehicleModel)
            binding.vehicleYeaManufacturerMet.setText(vehicleYearManufacture)
            binding.vehicleModelYearMet.setText(vehicleModelYear)
            binding.vehicleLicenceNumberGenericMet.setText(vehicleLicenceNumber)
            binding.vehicleLicenceTimeGenericMet.setText(convertDateToString(Date(vehicleLicenceTime)))
            binding.vehicleOvernightZipGenericMet.setText(overnightCEP)
            binding.vehicleRegisterNumMet.setText(vehicleRegisterNum)

            val spinnerPosition = brandsArrayAdapter.getPosition(vehicleBrand)
            binding.vehicleBrandGenericSpn.setSelection(spinnerPosition+1)
        }

    }

    private fun setRadioBox(genre: String) {
        when(genre) {
            "M" -> binding.maleGenericRb.isChecked = true
            "F" -> binding.femaleGenericRb.isChecked = true
        }
    }

    private fun setCivilStateSpn(civilState: String) {
        when(civilState) {
            CivilStateEnum.SINGLE.value -> { binding.civilStateGenericSpn.setSelection(1) }
            CivilStateEnum.MARRIED.value -> { binding.civilStateGenericSpn.setSelection(2) }
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

    private fun setupObservers() {

        mViewModel.getCurrentSaveStatus().observe(viewLifecycleOwner, object : Observer<SaveStatusEnum>{
            override fun onChanged(t: SaveStatusEnum?) {
                if (t == SaveStatusEnum.SUCCESS) {
                    "Salvo".makeShortToast(requireContext())
                    mViewModel.getCurrentSaveStatus().removeObserver(this)
                    if (editMode)
                        EventBus.getDefault().post(RefreshHistoricListEvent())
                    popBackStack()
                } else {
                    "Erro ao tentar Salvar".makeErrorShortToast(requireContext())
                    showContent(true)
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun makeDataPickerDialog(met: MaterialEditText) {

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, pYear, pMonth, pDayOfMonth ->
            val dayStr = if (pDayOfMonth < 10) "0$pDayOfMonth" else pDayOfMonth.toString()
            val monthStr = if (pMonth+1 < 10) "0${pMonth+1}" else (pMonth+1).toString()
            met.setText("$dayStr$monthStr$pYear")
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
        if (!validMaterialEditTextFilled(binding.vehicleModelNameGenericMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleYeaManufacturerMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleModelYearMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleRegisterNumMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleOvernightZipGenericMet, requireContext()))
            errors.add(true)

        return errors.isEmpty()

    }

    private fun validateCPF(): Boolean {

        var status = true

        if (binding.cpfGenericMet.text?.isEmpty() == true &&
            !Validador.CPF.ehValido(binding.cpfGenericMet.text.toString())) {

            binding.cpfGenericMet.error = "Erro no formato do CPF"
            status = false
        }

        return status
    }

    private fun confirmCancel() {

        val filledFields = arrayListOf<Boolean>()

        if (binding.fullNameGenericMet.text?.isNotEmpty() == true)
            filledFields.add(true)
        if (binding.cpfGenericMet.text?.isNotEmpty() == true)
            filledFields.add(true)
        if (binding.birthGenericMet.text?.isNotEmpty() == true)
            filledFields.add(true)
        if (binding.vehicleLicenceNumberGenericMet.text?.isNotEmpty() == true)
            filledFields.add(true)
        if (binding.vehicleLicenceTimeGenericMet.text?.isNotEmpty() == true)
            filledFields.add(true)
        if (binding.vehicleModelYearMet.text?.isNotEmpty() == true)
            filledFields.add(true)
        if (binding.vehicleYeaManufacturerMet.text?.isNotEmpty() == true)
            filledFields.add(true)
        if (binding.vehicleRegisterNumMet.text?.isEmpty() == true)
            filledFields.add(true)
        if (binding.vehicleOvernightZipGenericMet.text?.isNotEmpty() == true)
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

    private fun showContent(showContent: Boolean) {
        if (showContent) {
            binding.genericQuoteContent.visibility = View.VISIBLE
            binding.loadingContainer.visibility = View.GONE
        } else {
            binding.genericQuoteContent.visibility = View.GONE
            binding.loadingContainer.visibility = View.VISIBLE
        }
    }

    private fun popBackStack() = navController.popBackStack()

}