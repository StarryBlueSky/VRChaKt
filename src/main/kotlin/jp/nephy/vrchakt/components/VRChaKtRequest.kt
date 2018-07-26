package jp.nephy.vrchakt.components

import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import jp.nephy.jsonkt.JsonKt
import jp.nephy.jsonkt.contains
import jp.nephy.jsonkt.jsonObject
import jp.nephy.vrchakt.exceptions.VRChatApiError
import jp.nephy.vrchakt.models.Error
import jp.nephy.vrchakt.models.VRChaKtModel
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.ExecutorService

class VRChaKtRequest<M: VRChaKtModel>(private val httpClient: HttpClient, private val httpRequest: HttpRequestBuilder.() -> Unit, executor: ExecutorService, private val modelClass: Class<M>, private val isJsonArray: Boolean): ApiAction<M>(executor) {
    override fun complete(): VRChaKtResponse<M> {
        val response = runBlocking {
            httpClient.request<HttpResponse>(httpRequest)
        }
        val request = response.call.request

        val json = runBlocking {
            response.readText()
        }.let {
            if (isJsonArray) {
                JsonKt.toJsonArray(it).let {
                    jsonObject("items" to it)
                }
            } else {
                JsonKt.toJsonObject(it)
            }
        }

        if (json.contains("error")) {
            val error = try {
                JsonKt.parse<Error>(json)
            } catch (e: Exception) {
                null
            }

            throw VRChatApiError(error, request, response)
        }

        val result = modelClass.getConstructor(JsonObject::class.java).newInstance(json)
        return VRChaKtResponse(result, request, response)
    }
}
