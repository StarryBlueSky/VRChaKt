package jp.nephy.vrchakt.endpoints

import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.models.Avatar
import jp.nephy.vrchakt.models.User

@Suppress("UNUSED")
class Avatar(override val client: VRChaKtClient): VRChaKtEndpoint {
    fun show(id: String) = client.session.get("/avatars/$id").jsonObject<Avatar.Entry>()

    fun choose(id: String) = client.session.put("/avatar/$id/select").jsonObject<User.Detail>()

    fun list(user: Avatar.User? = null, featured: Boolean? = null, tag: List<String>? = null, search: String? = null, n: Int? = null, offset: Int? = null, order: Avatar.Order? = null, releaseStatus: Avatar.ReleaseStatus? = null, sort: Avatar.Sort? = null, maxUnityVersion: String? = null, minUnityVersion: String? = null, maxAssetVersion: String? = null, minAssetVersion: String? = null, platform: String? = null) = client.session.get("/avatars") {
        parameter(
                "user" to user?.value,
                "featured" to featured,
                "tag" to tag?.joinToString(","),
                "search" to search,
                "n" to n,
                "offset" to offset,
                "order" to order?.value,
                "releaseStatus" to releaseStatus?.value,
                "sort" to sort?.value,
                "maxUnityVersion" to maxUnityVersion,
                "minUnityVersion" to minUnityVersion,
                "maxAssetVersion" to maxAssetVersion,
                "minAssetVersion" to minAssetVersion,
                "platform" to platform
        )
    }.jsonArray<Avatar.Entry>()

    fun delete(id: String) = client.session.delete("/avatars/$id").jsonObject<Avatar.Entry>()
}
