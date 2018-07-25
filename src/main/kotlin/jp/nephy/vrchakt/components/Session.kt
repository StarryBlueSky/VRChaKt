package jp.nephy.vrchakt.components

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.userAgent
import jp.nephy.vrchakt.components.annotations.BasicAuthentication
import jp.nephy.vrchakt.models.RemoteConfig
import jp.nephy.vrchakt.models.VRChaKtModel
import java.io.Closeable
import java.util.concurrent.Executors

data class Session(val httpClient: HttpClient, val userAgent: String, val endpointVersion: EndpointVersion, val authenticationData: AuthenticationData): Closeable {
    val executor = Executors.newCachedThreadPool {
        Executors.defaultThreadFactory().newThread(it).also {
            it.isDaemon = true
        }
    }!!

    inline fun <reified M: VRChaKtModel> newCall(path: String, vararg params: Pair<String, Any?>, noinline builder: HttpRequestBuilder.() -> Unit = {  }): VRChaKtRequest<M> {
        var request: HttpRequestBuilder.() -> Unit = {
            builder()
            url {
                protocol = endpointVersion.protocol
                host = endpointVersion.host
                port = endpointVersion.protocol.defaultPort
                path(*endpointVersion.path, *path.removePrefix("/").split("/").toTypedArray())
                val queries = if (authenticationData.apiKey != null) {
                    params.toList() + ("apiKey" to authenticationData.apiKey)
                } else {
                    params.toList()
                }
                for (query in queries) {
                    val value = query.second?.toString() ?: continue
                    parameters.append(query.first, value)
                }
            }
            userAgent(userAgent)
        }

        val trace = Thread.currentThread().stackTrace.find { it.className.startsWith("jp.nephy.vrchakt.endpoint") }
        if (trace != null) {
            val method = javaClass.classLoader.loadClass(trace.className).methods.find { it.name == trace.methodName }
            if (method != null) {
                if (method.isAnnotationPresent(BasicAuthentication::class.java)) {
                    val previousRequestBuilder = request
                    request = {
                        previousRequestBuilder()
                        header(AuthenticationHandler.requiredAuthorizationFlag, "true")
                    }
                }
            }
        }

        return VRChaKtRequest(httpClient, request, executor, M::class.java)
    }

    val remoteConfig = newCall<RemoteConfig>("/config").complete().result.also {
        authenticationData.apiKey = it.apiKey
    }

    override fun close() {
        httpClient.close()
        executor.shutdownNow()
    }
}
