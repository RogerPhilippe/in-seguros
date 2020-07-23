package br.com.inseguros.data.sessions

import br.com.inseguros.data.model.User

object UserSession {

    private lateinit var mUserID: String
    private lateinit var mUserName: String
    private lateinit var mUserEmail: String
    private lateinit var mMessagingToken: String
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
    fun getUseTermSignUpState() =
        mUseTermSignUpState

    fun resetUserSession() {
        mUserName = ""
        mUserEmail = ""
        mUserID = ""
        mMessagingToken = ""
        mUseTermSignUpState = false
    }

    fun setUserID(userID: String) {
        mUserID = userID
    }
    fun getUserID() = mUserID

    fun setMessagingToken(messagingToken: String) {
        mMessagingToken = messagingToken
    }

    fun getMessagingToken() =
        mMessagingToken

    fun fillUser(user: User) {
        if (mUserID.isEmpty())
            mUserID = user.userID
        if (mUserName.isEmpty())
            mUserName = user.displayName
        if (mUserEmail.isEmpty())
            mUserEmail = user.userLogin
        if (mMessagingToken.isEmpty())
            mMessagingToken = user.messagingService
    }

}