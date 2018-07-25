package jp.nephy.vrchakt.models

import com.google.gson.JsonObject
import jp.nephy.jsonkt.byInt
import jp.nephy.jsonkt.byString

class Error(override val json: JsonObject): VRChaKtModel {
    val message by json.byString
    val statusCode by json.byInt("status_code")
}
