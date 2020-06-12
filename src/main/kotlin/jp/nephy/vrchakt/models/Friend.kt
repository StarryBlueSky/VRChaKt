@file:Suppress("UNUSED")

package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.*
import jp.nephy.jsonkt.delegation.*
import jp.nephy.vrchakt.models.User.DeveloperType
import jp.nephy.vrchakt.models.User.OnlineStatus
import jp.nephy.vrchakt.models.User.Tag

object Friend {
    data class User(override val json: JsonObject): VRChaKtModel {
        val currentAvatarImageUrl by string
        val currentAvatarThumbnailImageUrl by string
        val developerType by enum { DeveloperType.None }
        val displayName by string
        val id by string
        val location by string
        val status by enum { OnlineStatus.Active }
        val statusDescription by string
        val tags by enumList { listOf(Tag.Unknown) }
        val username by string
    }

    data class Status(override val json: JsonObject): VRChaKtModel {
        val isFriend by boolean
        val outgoingRequest by boolean
        val incomingRequest by boolean
    }
}
