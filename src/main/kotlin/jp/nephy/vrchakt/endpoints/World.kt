package jp.nephy.vrchakt.endpoints

import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.core.VRChaKtJsonArrayAction
import jp.nephy.vrchakt.models.Success
import jp.nephy.vrchakt.models.World

@Suppress("UNUSED")
class World(override val client: VRChaKtClient): VRChaKtEndpoint {
    fun show(id: String) = client.session.get("/worlds/$id").jsonObject<World.Detail>()

    fun list(filter: ListWorldFilter = ListWorldFilter.Any, featured: Boolean? = null, sort: World.Sort? = null, order: World.Order? = null, user: World.User? = null, userId: String? = null, n: Int? = null, offset: Int? = null, search: String? = null, tag: List<String>? = null, notag: List<String>? = null, releaseStatus: World.ReleaseStatus? = null, maxUnityVersion: String? = null, minUnityVersion: String? = null, maxAssetVersion: String? = null, minAssetVersion: String? = null, platform: String? = null): VRChaKtJsonArrayAction<World.Simple> {
        val path = when (filter) {
            ListWorldFilter.Any -> "/worlds"
            ListWorldFilter.Active -> "/worlds/active"
            ListWorldFilter.Recent -> "/worlds/recent"
            ListWorldFilter.Favorites -> "/worlds/favorites"
        }

        return client.session.get(path) {
            parameter(
                    "featured" to featured,
                    "sort" to sort?.value,
                    "order" to order?.value,
                    "user" to user?.value,
                    "userId" to userId,
                    "n" to n,
                    "offset" to offset,
                    "search" to search,
                    "tag" to tag?.joinToString(","),
                    "notag" to notag?.joinToString(","),
                    "releaseStatus" to releaseStatus?.value,
                    "maxUnityVersion" to maxUnityVersion,
                    "minUnityVersion" to minUnityVersion,
                    "maxAssetVersion" to maxAssetVersion,
                    "minAssetVersion" to minAssetVersion,
                    "platform" to platform
            )
        }.jsonArray()
    }

    fun delete(id: String) = client.session.delete("/worlds/$id").jsonObject<Success.Result>()

    fun metadata(id: String) = client.session.get("/worlds/$id/metadata").jsonObject<World.Metadata>()

    fun instance(id: String, instanceId: String) = client.session.get("/worlds/$id/$instanceId").jsonObject<World.Instance>()

    enum class ListWorldFilter {
        Any, Active, Recent, Favorites
    }
}
