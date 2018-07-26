package jp.nephy.vrchakt.exceptions

import io.ktor.client.request.HttpRequest
import io.ktor.client.response.HttpResponse
import io.ktor.http.fullPath

class VRChatApiError(val result: jp.nephy.vrchakt.models.Error?, val httpRequest: HttpRequest, val httpResponse: HttpResponse): Error() {
    override val message = if (result != null) {
        "Endpoint returned an error [${result.statusCode}: ${result.message}] while requesting ${httpRequest.url.protocol.name}://${httpRequest.url.host}${httpRequest.url.fullPath}."
    } else {
        "Endpoint returned unknown error while requesting ${httpRequest.url.protocol.name}://${httpRequest.url.host}${httpRequest.url.fullPath}."
    }
}
