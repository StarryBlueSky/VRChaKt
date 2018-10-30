package jp.nephy.vrchakt.endpoints

import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.models.Notification
import jp.nephy.vrchakt.models.Success

@Suppress("UNUSED")
class FriendRequest(override val client: VRChaKtClient): VRChaKtEndpoint {
    fun send(id: String) = client.session.post("/user/$id/friendRequest").jsonObject<Notification.Result>()

    fun accept(notificationId: String) = client.session.put("/auth/user/notifications/$notificationId/accept").jsonObject<Success.Result>()

    fun ignore(notificationId: String) = client.session.put("/auth/user/notifications/$notificationId/hide").jsonObject<Success.Result>()
}
