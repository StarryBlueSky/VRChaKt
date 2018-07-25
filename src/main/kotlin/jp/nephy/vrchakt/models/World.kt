package jp.nephy.vrchakt.models

import com.google.gson.JsonObject
import jp.nephy.jsonkt.*

class World(override val json: JsonObject): VRChaKtModel {
    val assetUrl by json.byString
    val authorId by json.byString
    val authorName by json.byString
    val capacity by json.byInt
    val description by json.byString
    val featured by json.byBool
    val id by json.byString
    val imageUrl by json.byString
    val instances by json.byJsonArray  // TODO
    val isLockdown by json.byBool
    val isSecure by json.byBool
    val name by json.byString
    val namespace by json.byString
    val occupants by json.byInt
    val organization by json.byString
    val pluginUrl by json.byString
    val releaseStatus by json.byString  // TODO
    val tags by json.byStringList  // TODO
    val thumbnailImageUrl by json.byString
    val totalLikes by json.byInt
    val totalVisits by json.byInt
    val unityPackageUpdated by json.byBool
    val unityPackageUrl by json.byString
    val unityPackages by json.byModelList<UnityPackages>()
    val version by json.byInt  // 2

    class UnityPackages(override val json: JsonObject): VRChaKtModel {
        val assetUrl by json.byString
        val assetVersion by json.byInt
        val id by json.byString
        val platform by json.byString
        val pluginUrl by json.byString
        val unitySortNumber by json.byInt
        val unityVersion by json.byString
    }
}
