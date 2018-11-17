package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.JsonObject

@Suppress("UNUSED")
object Notification {
    // TODO
    data class Result(override val json: JsonObject): VRChaKtModel
}
