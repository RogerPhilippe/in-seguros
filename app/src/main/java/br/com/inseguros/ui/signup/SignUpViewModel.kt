package br.com.inseguros.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.UserSession
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.User
import br.com.inseguros.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class SignUpViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val mUserRepository: UserRepository
) : ViewModel() {

    private val signUpStatus = MutableLiveData<SaveStatusEnum>()

    fun signUp(user: User) {
        auth.createUserWithEmailAndPassword(
            user.userLogin,
            user.passWD
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                user.userID = auth.currentUser?.uid ?: ""
                UserSession.fillUser(user)
                saveUser(user)
            }
        }
    }

    private fun saveUser(user: User) {

        db.collection("users")
            .document(user.userID)
            .set(user)
            .addOnSuccessListener { localSaveUser(user) }
            .addOnFailureListener { signUpStatus.postValue(SaveStatusEnum.ERROR) }
    }

    private fun localSaveUser(user: User) = runBlocking {

        if (mUserRepository.insert(user) > -1)
            signUpStatus.postValue(SaveStatusEnum.SUCCESS)
        else
            signUpStatus.postValue(SaveStatusEnum.ERROR)
    }

    fun getSignUpStatus() = signUpStatus

}