package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.ImmutableJsonObject
import jp.nephy.jsonkt.delegation.int
import jp.nephy.jsonkt.delegation.model
import jp.nephy.jsonkt.delegation.string

data class Error(override val json: ImmutableJsonObject): VRChaKtModel {
    val message by string
    val statusCode by int("status_code")

    data class Result(override val json: ImmutableJsonObject): VRChaKtModel {
        val error by model<Error>()
    }
}
