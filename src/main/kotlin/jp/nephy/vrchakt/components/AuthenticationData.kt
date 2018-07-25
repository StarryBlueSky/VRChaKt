package jp.nephy.vrchakt.components

class AuthenticationData {
    var cookie: String? = null
    // TODO: Automation
    fun cookie(value: String) {
        cookie = value
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
