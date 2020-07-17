package br.com.inseguros.data

object UserSession {

    private lateinit var mUserName: String
    private lateinit var mUserEmail: String

    fun setUserName(userName: String) {
        mUserName = userName
    }
    fun getUserName() = mUserName

    fun setUserEmail(userEmail: String) {
        mUserEmail = userEmail
    }
    fun getUserEmail() = mUserEmail

}