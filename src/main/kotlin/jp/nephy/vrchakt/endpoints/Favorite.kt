package jp.nephy.vrchakt.endpoints

import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.models.Favorite
import jp.nephy.vrchakt.models.Success
import jp.nephy.vrchakt.models.User
import jp.nephy.vrchakt.models.World

@Suppress("UNUSED")
class Favorite(override val client: VRChaKtClient): VRChaKtEndpoint {
    fun add(id: String, type: Favorite.Type) = client.session.post("/favorites") {
        body {
            form {
                add("favoriteId" to id, "type" to type.value)
            }
        }
    }.jsonArray<Favorite.Entry>()

    fun delete(id: String) = client.session.delete("/favorites/$id").jsonObject<Success.Result>()

    fun show(id: String) = client.session.get("/favorites/$id").jsonObject<Favorite.Entry>()

    fun list(type: Favorite.Type? = null) = client.session.get("/favorites") {
        parameter("type" to type?.value)
    }.jsonArray<Favorite.Entry>()

    fun listFriendFavorites(tag: Favorite.TagOption? = null) = client.session.get("/auth/user/friends/favorite") {
        parameter("tag" to tag?.value)
    }.jsonArray<User.Detail>()

    fun listWorldFavorites() = client.session.get("/worlds/favorites").jsonArray<World.Simple>()
}
