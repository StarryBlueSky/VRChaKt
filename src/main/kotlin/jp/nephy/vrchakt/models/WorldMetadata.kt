package jp.nephy.vrchakt.models

import com.google.gson.JsonObject
import jp.nephy.jsonkt.byJsonObject
import jp.nephy.jsonkt.byString

class WorldMetadata(override val json: JsonObject): VRChaKtModel {
    val id by json.byString
    val metadata by json.byJsonObject  // TODO
}
