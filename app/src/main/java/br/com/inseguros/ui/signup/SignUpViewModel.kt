package br.com.inseguros.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.inseguros.data.UserSession
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val signUpStatus = MutableLiveData<SaveStatusEnum>()

    fun signUp(user: User) {
        auth.createUserWithEmailAndPassword(
            user.userLogin,
            user.passWD
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                user.userID = auth.currentUser?.uid ?: ""
                UserSession.setUserEmail(user.userLogin)
                UserSession.setUserName(user.displayName)
                UserSession.setUserID(user.userID)
                saveUser(user)
            }
        }
    }

    private fun saveUser(user: User) {

        db.collection("users")
            .document(user.userID)
            .set(user)
            .addOnSuccessListener { signUpStatus.postValue(SaveStatusEnum.SUCCESS) }
            .addOnFailureListener { signUpStatus.postValue(SaveStatusEnum.ERROR) }
    }

    fun getSignUpStatus() = signUpStatus

}