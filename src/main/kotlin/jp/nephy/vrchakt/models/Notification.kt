package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.ImmutableJsonObject

@Suppress("UNUSED")
object Notification {
    // TODO
    data class Result(override val json: ImmutableJsonObject): VRChaKtModel
}
