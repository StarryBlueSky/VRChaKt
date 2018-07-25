package jp.nephy.vrchakt.exceptions

open class VRChaKtException(text: () -> String): Exception() {
    override val message = text()
}
