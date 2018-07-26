package jp.nephy.vrchakt.models

import com.google.gson.JsonObject
import jp.nephy.jsonkt.byBool
import jp.nephy.jsonkt.byInt
import jp.nephy.jsonkt.byModelList
import jp.nephy.jsonkt.byString

class WorldList(override val json: JsonObject): VRChaKtModel {
    val list by json.byModelList<World>(key = "items")

    class World(override val json: JsonObject): VRChaKtModel {
        val authorName by json.byString
        val capacity by json.byInt
        val id by json.byString
        val imageUrl by json.byString
        val isSecure by json.byBool
        val name by json.byString
        val occupants by json.byInt
        val organization by json.byString
        val releaseStatus by json.byString
        val thumbnailImageUrl by json.byString
        val totalLikes by json.byInt
        val totalVisits by json.byInt
    }
}
