package br.com.inseguros.di

import android.content.Context
import br.com.inseguros.data.DatabaseHandler
import br.com.inseguros.data.dao.*
import br.com.inseguros.data.repository.*
import br.com.inseguros.ui.historic.HistoricViewModel
import br.com.inseguros.ui.home.HomeViewModel
import br.com.inseguros.ui.login.LoginViewModel
import br.com.inseguros.ui.messages.MessagesViewModel
import br.com.inseguros.ui.quotes.genericscreen.QuoteGenericScreenViewModel
import br.com.inseguros.ui.quotes.house.QuoteHouseViewModel
import br.com.inseguros.ui.quotes.life.QuoteLifeViewModel
import br.com.inseguros.ui.quotesreceived.QuotesReceivedViewModel
import br.com.inseguros.ui.settings.SettingsViewModel
import br.com.inseguros.ui.signup.SignUpViewModel
import br.com.inseguros.ui.useterm.UseTermViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

// *************************************************************************************************
// * Database Methods
// *************************************************************************************************

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

private fun messageDAO(db: DatabaseHandler): MessageDAO {
    return db.messageDAO()
}

private fun messageRepository(dao: MessageDAO): MessageRepository {
    return MessageRepository(dao)
}

private fun messageContentDAO(db: DatabaseHandler): MessageContentDAO {
    return db.messageContentDAO()
}

private fun messageContentRepository(dao: MessageContentDAO): MessageContentRepository {
    return MessageContentRepository(dao)
}

// *************************************************************************************************
// * Firebase Methods
// *************************************************************************************************

private fun firebaseAuth() = FirebaseAuth.getInstance()

private fun firebaseDB() = FirebaseFirestore.getInstance()

private fun realtimeDatabase() = FirebaseDatabase.getInstance()

// *************************************************************************************************
// * Module Methods
// *************************************************************************************************

val viewModelModules = module {
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { QuotesReceivedViewModel(get()) }
    viewModel { MessagesViewModel() }
    viewModel { QuoteGenericScreenViewModel(get(), get(), get()) }
    viewModel { QuoteHouseViewModel() }
    viewModel { QuoteLifeViewModel() }
    viewModel { HistoricViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { UseTermViewModel(get()) }
    viewModel { SignUpViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get(), get()) }
}

val dbModule = module {
    single { database(get()) }
}

val daoModule = module {
    single { quoteVehicleDAO(get()) }
    single { userDAO(get()) }
    single { quotationProposalDAO(get()) }
    single { messageDAO(get()) }
    single { messageContentDAO(get()) }
}

val repositoryModule = module {
    single { quoteVehicleRepository(get()) }
    single { userRepository(get()) }
    single { quotationProposalRepository(get()) }
    single { messageRepository(get()) }
    single { messageContentRepository(get()) }
}

val firebaseModules = module {
    single { firebaseAuth() }
    single { firebaseDB() }
    single { realtimeDatabase() }
}