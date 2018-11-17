@file:Suppress("UNUSED")

package jp.nephy.vrchakt.core

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import jp.nephy.jsonkt.parse
import jp.nephy.vrchakt.models.RemoteConfig
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import mu.KLogger
import java.io.Closeable

data class Session(val httpClient: HttpClient, val dispatcher: ExecutorCoroutineDispatcher, internal val credentials: Credentials, val logger: KLogger, val option: ClientOption): Closeable {
    val remoteConfig = runBlocking {
        httpClient.get<String>("https://api.vrchat.cloud/api/1/config") {
            if (credentials.basic != null) {
                header(HttpHeaders.Authorization, "Basic ${credentials.basic}")
            }
        }.parse<RemoteConfig>()
    }

    val apiKey = remoteConfig.apiKey

    fun call(method: HttpMethod, path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return VRChaKtRequestBuilder(this, method, path).apply(builder).build()
    }

    fun get(path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return call(HttpMethod.Get, path, builder)
    }

    fun post(path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return call(HttpMethod.Post, path, builder)
    }

    fun put(path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return call(HttpMethod.Put, path, builder)
    }

    fun patch(path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return call(HttpMethod.Patch, path, builder)
    }

    fun delete(path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return call(HttpMethod.Delete, path, builder)
    }

    fun head(path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return call(HttpMethod.Head, path, builder)
    }

    fun options(path: String, builder: VRChaKtRequestBuilder.() -> Unit = {}): VRChaKtRequest {
        return call(HttpMethod.Options, path, builder)
    }

    override fun close() {
        httpClient.close()
        dispatcher.close()
    }
}
