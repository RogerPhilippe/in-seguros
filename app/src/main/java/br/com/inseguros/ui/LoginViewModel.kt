package br.com.inseguros.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.in_seguros_utils.makeErrorShortToast
import br.com.inseguros.data.model.User
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(
    private val auth: FirebaseAuth,
    private val context: Context
) : ViewModel() {

    private val currentUserAuthLiveData = MutableLiveData<User>()

    fun checkUserLogged() {
        if (auth.currentUser?.uid?.isNotEmpty() == true) {
            currentUserAuthLiveData.value = User(
                userID = auth.currentUser?.uid ?: "",
                displayName = auth.currentUser?.displayName ?: "",
                userLogin = auth.currentUser?.email ?: ""
            )
        }
    }

    fun logoutCurrentUser() {
        if (auth.currentUser?.uid?.isNotEmpty() == true)
            auth.signOut()
    }

    fun signInWithEmailAndPassword(user: User) {

        auth.signInWithEmailAndPassword(user.userLogin, user.passWD).addOnCompleteListener {
            if (it.isSuccessful) {
                user.userID = auth.currentUser?.uid ?: ""
                user.displayName = auth.currentUser?.displayName ?: ""
                currentUserAuthLiveData.postValue(user)
            } else {
                "Usuário e/ou senha errada!".makeErrorShortToast(context)
            }
        }
    }

    fun getCurrentUserAuthLiveData() = currentUserAuthLiveData

}