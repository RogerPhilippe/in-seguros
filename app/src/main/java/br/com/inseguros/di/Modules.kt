package br.com.inseguros.di

import android.content.Context
import br.com.inseguros.data.DatabaseHandler
import br.com.inseguros.data.dao.QuotationProposalDAO
import br.com.inseguros.data.dao.QuoteVehicleDAO
import br.com.inseguros.data.dao.UserDAO
import br.com.inseguros.data.repository.QuotationProposalRepository
import br.com.inseguros.data.repository.QuoteVehicleRepository
import br.com.inseguros.data.repository.UserRepository
import br.com.inseguros.ui.historic.HistoricViewModel
import br.com.inseguros.ui.home.HomeViewModel
import br.com.inseguros.ui.login.LoginViewModel
import br.com.inseguros.ui.messages.MessagesViewModel
import br.com.inseguros.ui.quotes.genericscreen.QuoteGenericScreenViewModel
import br.com.inseguros.ui.quotes.house.QuoteHouseViewModel
import br.com.inseguros.ui.quotes.life.QuoteLifeViewModel
import br.com.inseguros.ui.quotesreceived.QuotesReceivedViewModel
import br.com.inseguros.ui.signup.SignUpViewModel
import br.com.inseguros.ui.useterm.UseTermViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin

private fun database(context: Context): DatabaseHandler {
    return DatabaseHandler.getDatabase(context)
}

private fun quoteVehicleDAO(db: DatabaseHandler): QuoteVehicleDAO {
    return db.quoteVehicleDAO()
}

private fun quoteVehicleRepository(dao: QuoteVehicleDAO): QuoteVehicleRepository {
    return QuoteVehicleRepository(dao)
}

private fun userDAO(db: DatabaseHandler): UserDAO {
    return db.userDAO()
}

private fun userRepository(dao: UserDAO): UserRepository {
    return UserRepository(dao)
}

private fun quotationProposalDAO(db: DatabaseHandler): QuotationProposalDAO {
    return db.quotationProposalDAO()
}

private fun quotationProposalRepository(dao: QuotationProposalDAO): QuotationProposalRepository {
    return QuotationProposalRepository(dao)
}

private fun firebaseAuth() = FirebaseAuth.getInstance()

private fun firebaseDB() = FirebaseFirestore.getInstance()

private fun realtimeDatabase() = FirebaseDatabase.getInstance()

val viewModelModules = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { QuotesReceivedViewModel(get()) }
    viewModel { MessagesViewModel() }
    viewModel { QuoteGenericScreenViewModel(get(), get(), get()) }
    viewModel { QuoteHouseViewModel() }
    viewModel { QuoteLifeViewModel() }
    viewModel { HistoricViewModel(get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { UseTermViewModel(get()) }
    viewModel { SignUpViewModel(get(), get(), get()) }
}

val dbModule = module {
    single { database(get()) }
}

val daoModule = module {
    single { quoteVehicleDAO(get()) }
    single { userDAO(get()) }
    single { quotationProposalDAO(get()) }
}

val repositoryModule = module {
    single { quoteVehicleRepository(get()) }
    single { userRepository(get()) }
    single { quotationProposalRepository(get()) }
}

val firebaseModules = module {
    single { firebaseAuth() }
    single { firebaseDB() }
    single { realtimeDatabase() }
}