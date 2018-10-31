package jp.nephy.vrchakt.endpoints

import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.models.Friend
import jp.nephy.vrchakt.models.Success

@Suppress("UNUSED")
class Friend(override val client: VRChaKtClient): VRChaKtEndpoint {
    fun list(offset: Int? = null, n: Int? = null, offline: Boolean? = null) = client.session.get("/auth/user/friends") {
        parameter("offset" to offset, "n" to n, "offline" to offline)
    }.jsonArray<Friend.User>()

    fun friendStatus(id: String) = client.session.get("/user/$id/friendStatus").jsonObject<Friend.Status>()

    fun unfriend(id: String) = client.session.delete("/auth/user/friends/$id").jsonObject<Success.Result>()
}
