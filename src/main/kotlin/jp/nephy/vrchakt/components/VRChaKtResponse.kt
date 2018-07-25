package jp.nephy.vrchakt.components

import io.ktor.client.request.HttpRequest
import io.ktor.client.response.HttpResponse
import jp.nephy.vrchakt.models.VRChaKtModel
import java.io.Closeable

data class VRChaKtResponse<M: VRChaKtModel>(val result: M, val httpRequest: HttpRequest, val httpResponse: HttpResponse): Closeable {
    override fun close() {
        httpResponse.close()
    }
}
