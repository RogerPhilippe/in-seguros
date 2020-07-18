package br.com.inseguros.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.in_seguros_utils.makeErrorShortToast
import br.com.inseguros.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val context: Context
) : ViewModel() {

    private val currentUserAuthLiveData = MutableLiveData<User>()

    fun checkUserLogged() {
        if (auth.currentUser?.uid?.isNotEmpty() == true) {
            getUser(auth.currentUser?.uid ?: "")
        }
    }

    fun logoutCurrentUser() {
        if (auth.currentUser?.uid?.isNotEmpty() == true)
            auth.signOut()
    }

    fun signInWithEmailAndPassword(user: User) {

        auth.signInWithEmailAndPassword(user.userLogin, user.passWD).addOnCompleteListener {
            if (it.isSuccessful) {
                getUser(auth.currentUser?.uid ?: "")
            } else {
                "Usuário e/ou senha errada!".makeErrorShortToast(context)
            }
        }
    }

    private fun getUser(uid: String) {

        if (uid.isNotEmpty()) {
            db.collection("users")
                .document(uid)
                .get().addOnSuccessListener { doc ->
                    if (doc != null && doc.data?.isNotEmpty() == true) {
                        currentUserAuthLiveData.postValue(User(
                            userID = doc.data!!["userID"] as String,
                            displayName = doc.data!!["displayName"] as String,
                            userLogin = doc.data!!["userLogin"] as String
                        ))
                    }
                }
        }
    }

    fun getCurrentUserAuthLiveData() = currentUserAuthLiveData

}