package br.com.inseguros.ui.historic

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.inseguros.R
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.data.enums.QuoteTypeEnum
import br.com.inseguros.data.model.QuoteVehicle
import br.com.inseguros.databinding.HistoricFragmentBinding
import br.com.inseguros.events.RefreshHistoricListEvent
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.ui.utils.SwipeToDeleteCallback
import br.com.inseguros.utils.DialogFragmentUtil
import br.com.inseguros.utils.SnackBarUtil
import br.com.inseguros.utils.makeShortToast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

class HistoricFragment : BaseFragment() {

    private lateinit var binding: HistoricFragmentBinding
    override val layout = R.layout.historic_fragment
    private val viewModel: HistoricViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var adapter: HistoricAdapter
    private val items = mutableListOf<QuoteVehicle>()
    private val itemsToRemove = mutableListOf<QuoteVehicle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HistoricFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        val toolBarTitle = arguments?.get("tool_bar_title") as String
        binding.historicToolbar.title = toolBarTitle

        // historicRV
        adapter = HistoricAdapter(items, this)
        binding.historicRV.adapter = adapter
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                confirmUserIntent(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.historicRV)

        setupObservers()
        setupListeners()

        viewModel.loadCurrentQuotesVehicle()

    }

    private fun setupObservers() {

        viewModel.getCurrentQuotesVehicleLiveData().observe(viewLifecycleOwner, object : Observer<List<QuoteVehicle>>{
            override fun onChanged(list: List<QuoteVehicle>) {
                if (list.isNotEmpty()) {
                    items.clear()
                    items.addAll(list)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.getCurrentOPQuoteStatus().observe(viewLifecycleOwner, object : Observer<String>{
            override fun onChanged(status: String) {
                status.makeShortToast(requireContext())
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun setupListeners() {

        binding.historicToolbar.setNavigationOnClickListener {
            this.popBackStack()
        }
    }

    private fun popBackStack() = navController.popBackStack()

    fun editQuote(item: QuoteVehicle) {

        val bundle = bundleOf("quote_vehicle" to item, "main_sub_menu" to MainSubMenu(title = "Editar ${item.vehicleModel} ${item.vehicleModelYear}"))
        navController.navigate(R.id.action_historicFragment_to_quoteGenericScreenFragment, bundle)

    }

    fun cancelQuote(item: QuoteVehicle) {

        DialogFragmentUtil(
            "Deseja cancelar a quotação ${item.vehicleType} ${item.vehicleModel} ${item.vehicleModelYear}",
            {
                item.apply {
                    this.quoteStatus = QuoteTypeEnum.CANCELED.value
                }
                viewModel.cancelCurrentQuote(item)
            },
            {}
        ).show(parentFragmentManager, "quote_cancel_dialog_fragment")

    }

    private fun confirmUserIntent(position: Int) {

        DialogFragmentUtil("Deseja excluir o item?",
            { adapter.removeAt(position) },
            { adapter.notifyDataSetChanged() }
        ).show(parentFragmentManager, "remove_item_dialog")
    }

    fun cancelItemDeletion(position: Int, item: QuoteVehicle) {
        itemsToRemove.add(item)
        SnackBarUtil().create(requireActivity(), getString(R.string.item_removed_label)) {
            itemsToRemove.removeAt(itemsToRemove.size - 1)
            adapter.undoItemRemoved(position, item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (itemsToRemove.size > 0) {
            viewModel.deleteQuotes(itemsToRemove)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateEvent(event: RefreshHistoricListEvent) {
        viewModel.loadCurrentQuotesVehicle()
    }

}