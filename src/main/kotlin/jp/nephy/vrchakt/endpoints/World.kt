package jp.nephy.vrchakt.endpoints

import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.components.annotations.BasicAuthentication
import jp.nephy.vrchakt.models.ListWorldFilter
import jp.nephy.vrchakt.models.World

class World(override val client: VRChaKtClient): VRChaKtEndpoint {
    @BasicAuthentication
    fun getById(id: String) = client.session.newCall<World>("/worlds/$id")

    fun list(filter: ListWorldFilter = ListWorldFilter.Any) {
        // TODO
    }

    fun delete(id: String) {
        // TODO
    }

    fun getMetadata(id: String) {
        // TODO
    }
}
