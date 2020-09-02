package br.com.inseguros.ui.signup

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.in_seguros_utils.makeErrorShortToast
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.User
import br.com.inseguros.data.repository.UserRepository
import br.com.inseguros.data.sessions.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class SignUpViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val realtimeDatabase: FirebaseDatabase,
    private val mUserRepository: UserRepository,
    private val context: Context
) : ViewModel() {

    private val signUpStatus = MutableLiveData<SaveStatusEnum>()

    fun signUp(user: User) {
        auth.createUserWithEmailAndPassword(
            user.userLogin,
            user.passWD
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user.userID = auth.currentUser?.uid ?: ""
                    UserSession.fillUser(user)
                    saveUser(user)
                } else
                    signUpStatus.postValue(SaveStatusEnum.UNKNOWN)
            }
            .addOnFailureListener {
                signUpStatus.postValue(SaveStatusEnum.UNKNOWN)
            }
    }

    private fun saveUser(user: User) {

        db.collection("users")
            .document(user.userID)
            .set(user)
            .addOnSuccessListener { notifyNewUserInRealtimeDB(user) }
            .addOnFailureListener { signUpStatus.postValue(SaveStatusEnum.ERROR) }
    }

    private fun notifyNewUserInRealtimeDB(user: User) {

        realtimeDatabase.reference
            .child("new_user")
            .child(user.userID)
            .child("user_id")
            .setValue(user.userID)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    localSaveUser(user)
                else
                    signUpStatus.postValue(SaveStatusEnum.ERROR)
            }
            .addOnFailureListener {
                it.message?.makeErrorShortToast(context)
                signUpStatus.postValue(SaveStatusEnum.ERROR)
            }

    }

    private fun localSaveUser(user: User) = runBlocking {

        if (mUserRepository.insert(user) > -1)
            signUpStatus.postValue(SaveStatusEnum.SUCCESS)
        else
            signUpStatus.postValue(SaveStatusEnum.ERROR)
    }

    fun getSignUpStatus() = signUpStatus

}