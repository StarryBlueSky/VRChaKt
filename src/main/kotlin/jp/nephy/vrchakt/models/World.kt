@file:Suppress("UNUSED")

package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.JsonObject
import jp.nephy.jsonkt.delegation.*
import jp.nephy.jsonkt.parseList
import jp.nephy.jsonkt.string
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull

object World {
    data class Detail(override val json: JsonObject): VRChaKtModel {
        val assetUrl by string
        val authorId by string
        val authorName by string
        val capacity by int
        val description by string
        val featured by boolean
        val id by string
        val imageUrl by string
        val instances by lambdaList {
            val array = it.jsonArray
            Instance(array[0].string, array[1].intOrNull ?: 0)
        }
        val isLockdown by boolean
        val isSecure by boolean
        val name by string
        val namespace by string
        val occupants by int
        val organization by string
        val pluginUrl by string
        val releaseStatus by enum(ReleaseStatus::class, default = ReleaseStatus.Public)
        val tags by stringList
        val thumbnailImageUrl by string
        val totalLikes by int
        val totalVisits by int
        val unityPackageUpdated by boolean
        val unityPackageUrl by string
        val unityPackages by modelList<UnityPackage>()
        val version by int

        data class Instance(val id: String, val playerCount: Int)

        data class UnityPackage(override val json: JsonObject): VRChaKtModel {
            val assetUrl by string
            val assetVersion by int
            val createdAt by string("created_at")
            val id by string
            val platform by string
            val pluginUrl by string
            val unitySortNumber by int
            val unityVersion by string
        }
    }

    data class Simple(override val json: JsonObject): VRChaKtModel {
        val authorName by string
        val capacity by int
        val id by string
        val imageUrl by string
        val isSecure by boolean
        val name by string
        val occupants by int
        val organization by string
        val releaseStatus by string
        val thumbnailImageUrl by string
        val totalLikes by int
        val totalVisits by int
        val tags by stringList
    }

    data class Instance(override val json: JsonObject): VRChaKtModel {
        val friends by lambda {
            if (it.booleanOrNull == false) {
                emptyList()
            } else {
                it.jsonArray.parseList<User>()
            }
        }
        val id by string
        val name by string
        val private by lambda {
            if (it.booleanOrNull == false) {
                emptyList()
            } else {
                it.jsonArray.parseList<User>()
            }
        }
        val users by lambda {
            if (it.booleanOrNull == false) {
                emptyList()
            } else {
                it.jsonArray.parseList<User>()
            }
        }

        data class User(override val json: JsonObject): JsonModel {
            val currentAvatarImageUrl by string
            val currentAvatarThumbnailImageUrl by string
            val developerType by string
            val displayName by string
            val id by string
            val networkSessionId by string
            val status by string
            val statusDescription by string
            val tags by stringList
            val username by string
        }
    }

    data class Metadata(override val json: JsonObject): VRChaKtModel {
        val id by string
        val metadata by stringList
    }

    enum class Tag(override val value: String): JsonEnum<String> {
        FriendsPlus("hidden"), Friends("friends"),
        Invite("private"), InvitePlus("canRequestInvite")
    }

    enum class ReleaseStatus(override val value: String): JsonEnum<String> {
        Public("public"),
        Private("private"),
        All("all"),
        Hidden("hidden")
    }

    enum class User(override val value: String): JsonEnum<String> {
        Me("me"), Friends("friends")
    }

    enum class Sort(override val value: String): JsonEnum<String> {
        Popularity("popularity"),
        Created("created"),
        Updated("updated"),
        Order("order"),
        _createdAt("_created_at"),
        _updatedAt("_updated_at")
    }

    enum class Order(override val value: String): JsonEnum<String> {
        Ascending("ascending"), Descending("descending")
    }
}
