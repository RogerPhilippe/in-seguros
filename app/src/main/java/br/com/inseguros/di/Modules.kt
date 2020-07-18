package br.com.inseguros.di

import android.content.Context
import br.com.inseguros.data.DatabaseHandler
import br.com.inseguros.data.dao.QuoteVehicleDAO
import br.com.inseguros.data.repository.ParentRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import br.com.inseguros.ui.login.LoginViewModel
import br.com.inseguros.ui.historic.HistoricViewModel
import br.com.inseguros.ui.home.HomeViewModel
import br.com.inseguros.ui.messages.MessagesViewModel
import br.com.inseguros.ui.quotes.genericscreen.QuoteGenericScreenViewModel
import br.com.inseguros.ui.quotes.house.QuoteHouseViewModel
import br.com.inseguros.ui.quotes.life.QuoteLifeViewModel
import br.com.inseguros.ui.quotesreceived.QuotesReceivedViewModel
import br.com.inseguros.ui.signup.SignUpViewModel
import br.com.inseguros.ui.useterm.UseTermViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

private fun database(context: Context): DatabaseHandler {
    return DatabaseHandler.getDatabase(context)
}

private fun quoteVehicleDAO(db: DatabaseHandler): QuoteVehicleDAO {
    return db.quoteVehicleDAO()
}

private fun quoteVehicleRepository(dao: QuoteVehicleDAO): QuoteVehicleRepository {
    return QuoteVehicleRepository(dao)
}

private fun firebaseAuth() = FirebaseAuth.getInstance()

private fun firebaseDB() = FirebaseFirestore.getInstance()

val viewModelModules = module {
    viewModel { HomeViewModel() }
    viewModel { QuotesReceivedViewModel() }
    viewModel { MessagesViewModel() }
    viewModel { QuoteGenericScreenViewModel(get()) }
    viewModel { QuoteHouseViewModel() }
    viewModel { QuoteLifeViewModel() }
    viewModel { HistoricViewModel(get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { UseTermViewModel(get()) }
    viewModel { SignUpViewModel(get(), get()) }
}

val dbModule = module {
    single { database(get()) }
}

val daoModule = module {
    single { quoteVehicleDAO(get()) }
}

val repositoryModule = module {
    single<ParentRepository> { quoteVehicleRepository(get()) }
}

val firebaseModules = module {
    single { firebaseAuth() }
    single { firebaseDB() }
}