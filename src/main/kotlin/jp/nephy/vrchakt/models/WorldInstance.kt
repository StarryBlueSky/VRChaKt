package jp.nephy.vrchakt.models

import com.google.gson.JsonObject
import jp.nephy.jsonkt.*

class WorldInstance(override val json: JsonObject): VRChaKtModel {
    val friends by json.byLambda {
        if (nullableBool == false) {
            emptyList()
        } else {
            JsonKt.parseList<User>(jsonArray)
        }
    }
    val id by json.byString
    val name by json.byString
    val private by json.byLambda {
        if (nullableBool == false) {
            emptyList()
        } else {
            JsonKt.parseList<User>(jsonArray)
        }
    }
    val users by json.byLambda {
        if (nullableBool == false) {
            emptyList()
        } else {
            JsonKt.parseList<User>(jsonArray)
        }
    }

    class User(override val json: JsonObject): JsonModel {
        val currentAvatarImageUrl by json.byString
        val currentAvatarThumbnailImageUrl by json.byString
        val developerType by json.byString
        val displayName by json.byString
        val id by json.byString
        val networkSessionId by json.byString
        val status by json.byString
        val statusDescription by json.byString
        val tags by json.byJsonArray  // TODO
        val username by json.byString
    }
}
