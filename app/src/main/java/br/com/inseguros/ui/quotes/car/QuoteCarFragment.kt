package br.com.inseguros.ui.quotes.car

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.databinding.QuoteCarFragmentBinding
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.utils.validMaterialEditTextFilled

class QuoteCarFragment : BaseFragment() {

    private lateinit var genreRadioBox: String
    private lateinit var languages: Array<String>
    private lateinit var binding: QuoteCarFragmentBinding
    override val layout = R.layout.quote_car_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuoteCarFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val mainSubMenu = arguments?.get("main_sub_menu") as MainSubMenu
        binding.quotesCarToolbar.title = mainSubMenu.title

        val decodedString = Base64.decode(mainSubMenu.menuIcon, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.quoteCarIconIv.setImageBitmap(decodedByte)

        val description = "Seu carro seguro com a Youse pra curtir a vida numa boa. Você personaliza as coberturas e assistências do seu Seguro Auto online e paga só pelo que escolhe."
        binding.quoteCarDescriptionTv.text = description

        languages = arrayOf("Solteiro", "Casado")
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, languages)
        binding.civilStateSpn.adapter = arrayAdapter

        setupListeners()

    }

    private fun setupListeners() {

        binding.quotesCarToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

        binding.saveBtn.setOnClickListener {
            when(binding.quoteCarRg.checkedRadioButtonId) {
                R.id.male_rb -> { Toast.makeText(requireContext(), "Masculino", Toast.LENGTH_SHORT).show() }
                R.id.female_rb -> { Toast.makeText(requireContext(), "Feminino", Toast.LENGTH_SHORT).show() }
            }
            validateFields()
        }

        binding.civilStateSpn.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Selecione uma opção", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genreRadioBox = languages[position]
            }

        }

    }

    private fun validateFields(): Boolean {

        val errors = arrayListOf<Boolean>()

        if (!validMaterialEditTextFilled(binding.fullNameMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.cpfMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.birthMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleLicenceNumberMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleLicenceTimeMet, requireContext()))
            errors.add(true)
        if (!validMaterialEditTextFilled(binding.vehicleOvernightZipMet, requireContext()))
            errors.add(true)

        return errors.isEmpty()

    }

}