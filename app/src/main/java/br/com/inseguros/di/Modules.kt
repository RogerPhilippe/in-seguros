package br.com.inseguros.di

import br.com.inseguros.ui.home.HomeViewModel
import br.com.inseguros.ui.messages.MessagesViewModel
import br.com.inseguros.ui.quotes.car.QuoteCarViewModel
import br.com.inseguros.ui.quotes.house.QuoteHouseViewModel
import br.com.inseguros.ui.quotes.life.QuoteLifeViewModel
import br.com.inseguros.ui.quotes.motorcycle.QuoteMotocycleViewModel
import br.com.inseguros.ui.quotesreceived.QuotesReceivedViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { HomeViewModel() }
    viewModel { QuotesReceivedViewModel() }
    viewModel { MessagesViewModel() }
    viewModel { QuoteCarViewModel() }
    viewModel { QuoteMotocycleViewModel() }
    viewModel { QuoteHouseViewModel() }
    viewModel { QuoteLifeViewModel() }
}