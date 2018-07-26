package jp.nephy.vrchakt.endpoints

import io.ktor.http.HttpMethod
import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.components.AuthenticationRequred
import jp.nephy.vrchakt.components.VRChaKtRequest
import jp.nephy.vrchakt.models.*
import jp.nephy.vrchakt.models.World
import jp.nephy.vrchakt.models.parameters.*

class World(override val client: VRChaKtClient): VRChaKtEndpoint {
    @AuthenticationRequred
    fun getById(id: String) = client.session.newCall<World>("/worlds/$id")

    @AuthenticationRequred
    fun list(filter: ListWorldFilter = ListWorldFilter.Any, featured: Boolean? = null, sort: SortOptions? = null, user: UserOptions? = null, userId: String? = null, n: Int? = null, offset: Int? = null, search: String? = null, tag: Array<WorldTag>? = null, notag: Array<WorldTag>? = null, releaseStatus: ReleaseStatus? = null, maxUnityVersion: String? = null, minUnityVersion: String? = null, maxAssetVersion: String? = null, minAssetVersion: String? = null, platform: String? = null): VRChaKtRequest<WorldList> {
        val path = when (filter) {
            ListWorldFilter.Any -> "/worlds"
            ListWorldFilter.Active -> "/worlds/active"
            ListWorldFilter.Recent -> "/worlds/recent"
            ListWorldFilter.Favorites -> "/worlds/favorites"
        }

        return client.session.newCall(path, true, "featured" to featured, "sort" to sort?.key, "user" to user?.key, "userId" to userId, "n" to n, "offset" to offset, "search" to search, "tag" to tag?.joinToString(",") { it.key }, "notag" to notag?.joinToString(",") { it.key }, "releaseStatus" to releaseStatus?.key, "maxUnityVersion" to maxUnityVersion, "minUnityVersion" to minUnityVersion, "maxAssetVersion" to maxAssetVersion, "minAssetVersion" to minAssetVersion, "platform" to platform)
    }

    @AuthenticationRequred
    fun delete(id: String) = client.session.newCall<Unknown>("/worlds/$id") {
        method = HttpMethod.Delete
    }

    @AuthenticationRequred
    fun getMetadata(id: String) = client.session.newCall<WorldMetadata>("/worlds/$id/metadata")

    @AuthenticationRequred
    fun getInstance(id: String, instanceId: String) = client.session.newCall<WorldInstance>("/worlds/$id/$instanceId")
}
