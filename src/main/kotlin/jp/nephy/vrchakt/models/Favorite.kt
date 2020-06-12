@file:Suppress("UNUSED")

package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.*
import jp.nephy.jsonkt.delegation.*

object Favorite {
    data class Entry(override val json: JsonObject): VRChaKtModel {
        val id by string
        val type by enum { Type.World }
        val favoriteId by string
        val tags by stringList
    }

    enum class Type(override val value: String): JsonEnum<String> {
        World("world"), Friend("friend"), Avatar("avatar")
    }

    enum class TagOption(override val value: String): JsonEnum<String> {
        Group0("group_0"), Group1("group_1"), Group2("group_2")
    }
}
