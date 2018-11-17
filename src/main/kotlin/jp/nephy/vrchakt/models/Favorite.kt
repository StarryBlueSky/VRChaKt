@file:Suppress("UNUSED")

package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.JsonObject
import jp.nephy.jsonkt.delegation.JsonEnum
import jp.nephy.jsonkt.delegation.enum
import jp.nephy.jsonkt.delegation.string
import jp.nephy.jsonkt.delegation.stringList

object Favorite {
    data class Entry(override val json: JsonObject): VRChaKtModel {
        val id by string
        val type by enum(Type::class, default = Type.World)
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
