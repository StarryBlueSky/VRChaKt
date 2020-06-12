@file:Suppress("UNUSED")

package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.*
import jp.nephy.jsonkt.delegation.*

object Avatar {
    data class Entry(override val json: JsonObject): VRChaKtModel {
        val id by string
        val name by string
        val description by string
        val authorId by string
        val authorName by string
        val tags by stringList
        val assetUrl by string
        val imageUrl by string
        val thumbnailImageUrl by string
        val releaseStatus by enum { ReleaseStatus.Public }
        val version by int
        val featured by boolean
        val unityPackages by modelList<UnityPackage>()
        val unityPackageUpdated by boolean
        val unityPackageURL by string

        data class UnityPackage(override val json: JsonObject): VRChaKtModel {
            val id by string
            val assetUrl by string
            val unityVersion by string
            val unitySortNumber by int
            val assetVersion by int
            val platform by string
            val createdAt by string("created_at")
        }
    }

    enum class ReleaseStatus(override val value: String): JsonEnum<String> {
        Public("public"), Private("private"), Hidden("hidden"), All("all")
    }

    enum class Order(override val value: String): JsonEnum<String> {
        Ascending("ascending"), Descending("descending")
    }

    enum class User(override val value: String): JsonEnum<String> {
        Me("me"), Friends("friends")
    }

    enum class Sort(override val value: String): JsonEnum<String> {
        Created("created"),
        Updated("updated"),
        Order("order"),
        _createdAt("_created_at"),
        _updatedAt("_updated_at")
    }
}
