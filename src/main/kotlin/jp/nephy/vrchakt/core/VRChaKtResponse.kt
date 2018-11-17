@file:Suppress("UNUSED") @file:JvmName("VRChaKtResponseKt")

package jp.nephy.vrchakt.core

import io.ktor.client.request.HttpRequest
import io.ktor.client.response.HttpResponse
import jp.nephy.jsonkt.JsonArray
import jp.nephy.jsonkt.JsonObject
import jp.nephy.jsonkt.jsonArrayOf
import jp.nephy.vrchakt.models.VRChaKtModel
import java.io.Closeable
import kotlin.reflect.KClass

interface VRChaKtResponse: Closeable {
    val request: HttpRequest
    val response: HttpResponse
    val action: VRChaKtAction

    override fun close() {
        response.close()
    }
}

private interface JsonResponse<M: VRChaKtModel, T: Any> {
    val model: KClass<M>
    val json: T
}

private interface CompletedResponse {
    val content: String
}

data class VRChaKtJsonObjectResponse<M: VRChaKtModel>(override val model: KClass<M>, val result: M, override val request: HttpRequest, override val response: HttpResponse, override val content: String, override val action: VRChaKtAction): VRChaKtResponse, JsonResponse<M, JsonObject>, CompletedResponse {
    override val json by lazy { result.json }
}

data class VRChaKtJsonArrayResponse<M: VRChaKtModel>(override val model: KClass<M>, override val request: HttpRequest, override val response: HttpResponse, override val content: String, override val action: VRChaKtAction): VRChaKtResponse, JsonResponse<M, JsonArray>, CompletedResponse, ArrayList<M>() {
    override val json by lazy { jsonArrayOf(*map { it.json }.toTypedArray()) }
}

data class VRChaKtTextResponse(override val request: HttpRequest, override val response: HttpResponse, override val content: String, override val action: VRChaKtAction): VRChaKtResponse, CompletedResponse
