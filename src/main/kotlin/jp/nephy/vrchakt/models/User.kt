@file:Suppress("UNUSED")

package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.*
import jp.nephy.jsonkt.delegation.*

object User {
    data class CurrentUser(override val json: JsonObject): VRChaKtModel {
        val acceptedTOSVersion by int
        val currentAvatar by string
        val currentAvatarAssetUrl by string
        val currentAvatarImageUrl by string
        val currentAvatarThumbnailImageUrl by string
        val developerType by enum { DeveloperType.None }
        val displayName by string
        val emailVerified by boolean
        val friendGroupNames by jsonArray
        val friends by stringList
        val hasBirthday by boolean
        val hasEmail by boolean
        val hasLoggedInFromClient by boolean
        val hasPendingEmail by boolean
        val homeLocation by string
        val id by string
        val lastLogin by string("last_login")
        val obfuscatedEmail by string
        val obfuscatedPendingEmail by string
        val pastDisplayNames by modelList<PastDisplayName>()
        val status by enum { OnlineStatus.Active }
        val statusDescription by string
        val steamDetails by model<SteamDetails>()
        val tags by enumList { listOf(Tag.Unknown) }
        val unsubscribe by boolean
        val username by string
    }

    data class Detail(override val json: JsonObject): VRChaKtModel {
        val currentAvatarImageUrl by string
        val currentAvatarThumbnailImageUrl by string
        val developerType by enum { DeveloperType.None }
        val displayName by string
        val id by string
        val instanceId by nullableString
        val lastLogin by string("last_login")
        val location by nullableString
        val status by enum { OnlineStatus.Active }
        val statusDescription by string
        val tags by enumList { listOf(Tag.Unknown) }
        val username by string
        val worldId by nullableString
    }

    data class Simple(override val json: JsonObject): VRChaKtModel {
        val id by string
        val username by string
        val displayName by string
        val currentAvatarImageUrl by string
        val currentAvatarThumbnailImageUrl by string
        val tags by enumList { listOf(Tag.Unknown) }
        val developerType by enum { DeveloperType.None }
    }

    enum class OnlineStatus(override val value: String): JsonEnum<String> {
        Active("active"),
        JoinMe("join_me"),
        Busy("busy"),
        Offline("offline")
    }

    // From https://vrchatapi.github.io/#/UserAPI/Tags
    enum class Tag(override val value: String): JsonEnum<String> {
        SystemScriptingAccess("system_scripting_access"), AdminScriptingAccess("admin_scripting_access"),

        SystemAvatarAccess("system_avatar_access"), AdminAvatarAccess("admin_avatar_access"), AdminAvatarRestricted("admin_avatar_restricted"),

        SystemWorldAccess("system_world_access"), AdminWorldAccess("admin_world_access"), AdminWorldRestricted("admin_world_restricted"),

        AdminModerator("admin_moderator"),

        SystemFeedbackAccess("system_feedback_access"),

        SystemTrustBasic("system_trust_basic"), SystemTrustIntermediate("system_trust_intermediate"), SystemTrustKnown("system_trust_known"), SystemTrustTrusted("system_trust_trusted"), SystemTrustVeteran("system_trust_veteran"),

        SystemLegend("system_legend"),

        SystemProbableTroll("system_probable_troll"), SystemTroll("system_troll"),

        AdminLockLevel("admin_lock_level"), AdminLockTags("admin_lock_tags"),

        AdminOfficialThumbnail("admin_official_thumbnail"),

        Unknown("unknown"),

        ShowSocialRank("show_social_rank")
    }

    enum class DeveloperType(override val value: String): JsonEnum<String> {
        None("none"), Trusted("trusted"), Internal("internal"), Moderator("moderator")
    }

    data class PastDisplayName(override val json: JsonObject): VRChaKtModel {
        val displayName by string
        val updatedAt by string("updated_at")
    }

    // TODO
    data class SteamDetails(override val json: JsonObject): VRChaKtModel
}
