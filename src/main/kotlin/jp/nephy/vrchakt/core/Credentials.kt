package jp.nephy.vrchakt.core

import jp.nephy.vrchakt.core.i18n.LocalizedString
import java.util.*

@Suppress("UNUSED")
data class Credentials(val cookie: String?, val basic: String?) {
    class Builder {
        private var cookie: String? = null
        fun cookie(value: String) {
            cookie = value
        }

        private var basic: String? = null
        fun user(vrchatUsername: String, vrchatPassword: String) {
            basic = Base64.getEncoder().encodeToString("$vrchatUsername:$vrchatPassword".toByteArray())
        }

        internal fun build(): Credentials {
            if (cookie == null && basic == null) {
                throw VRChaKtLocalizedException(LocalizedString.CredentialsAreAllNull)
            }

            return Credentials(cookie, basic)
        }
    }
}
