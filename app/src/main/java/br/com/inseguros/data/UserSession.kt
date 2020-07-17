package br.com.inseguros.data

object UserSession {

    private lateinit var mUserName: String
    private lateinit var mUserEmail: String
    private var mUseTermSignUpState: Boolean = false

    fun setUserName(userName: String) {
        mUserName = userName
    }
    fun getUserName() = mUserName

    fun setUserEmail(userEmail: String) {
        mUserEmail = userEmail
    }
    fun getUserEmail() = mUserEmail

    fun setUseTermSignUpState(state: Boolean) {
        mUseTermSignUpState = state
    }
    fun getUseTermSignUpState() = mUseTermSignUpState

    fun resetUserSession() {
        mUserName = ""
        mUserEmail = ""
        mUseTermSignUpState = false
    }

}