package jp.nephy.vrchakt.components

class AuthenticationData {
    var token: String? = null
    // TODO: Automation
    fun token(value: String) {
        token = value
    }

    // TODO
    var username: String? = null
    var password: String? = null
    fun user(newUsername: String, newPassword: String) {
        username = newUsername
        password = newPassword
    }

    var apiKey: String? = null
    fun apiKey(value: String) {
        apiKey = value
    }
}
