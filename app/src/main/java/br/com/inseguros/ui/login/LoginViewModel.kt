package br.com.inseguros.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import br.com.in_seguros_utils.makeErrorShortToast
import br.com.inseguros.data.UserSession
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.User
import br.com.inseguros.data.repository.UserRepository
import br.com.inseguros.data.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.runBlocking

class LoginViewModel(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
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
                currentUserAuthLiveData.postValue(User())
            }
        }
    }

    private fun getUser(uid: String) = runBlocking {

        if (uid.isNotEmpty()) {
            val user = userRepository.findAllByID(uid)
            if (user != null) {
                saveUserRecoveredByFirebase(user)
                currentUserAuthLiveData.postValue(user)
            }
            else getUserByFirebase(uid)
        }
    }

    private fun getUserByFirebase(uid: String) {

        db.collection("users")
            .document(uid)
            .get().addOnSuccessListener { doc ->
                if (doc != null && doc.data?.isNotEmpty() == true) {
                    val user = User(
                        userID = doc.data!!["userID"] as String,
                        displayName = doc.data!!["displayName"] as String,
                        userLogin = doc.data!!["userLogin"] as String,
                        phone = doc.data!!["phone"] as String,
                        messagingService = doc.data!!["messagingService"] as String
                    )
                    saveUserRecoveredByFirebase(user)
                }
            }
    }

    private fun saveUserRecoveredByFirebase(user: User) = runBlocking {

        checkIfMustUpdateTokenInFirebase(user)

        if (userRepository.insert(user) > -1)
            currentUserAuthLiveData.postValue(user)
        else
            currentUserAuthLiveData.postValue(User())
    }

    private fun checkIfMustUpdateTokenInFirebase(user: User) {

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val token = prefs.getString(Constants.NEW_TOKEN_KEY, "")
        if (!token.isNullOrEmpty() && token != user.messagingService) {
            user.messagingService = token
            UserSession.setMessagingToken(token)
            mergeUser(user)
        }
    }

    private fun mergeUser(user: User) {

        db.collection("users")
            .document(user.userID)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { Log.i("Login Update FireStore", SaveStatusEnum.SUCCESS.name) }
            .addOnFailureListener { Log.e("Err: luf-01", SaveStatusEnum.ERROR.name) }
    }

    fun getCurrentUserAuthLiveData() = currentUserAuthLiveData

}