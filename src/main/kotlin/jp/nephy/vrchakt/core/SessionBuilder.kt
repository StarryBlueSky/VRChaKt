package jp.nephy.vrchakt.core

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.features.HttpPlainText
import io.ktor.client.features.cookies.AcceptAllCookiesStorage
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.cookies.addCookie
import io.ktor.http.Cookie
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.util.concurrent.TimeUnit

@Suppress("UNUSED")
class SessionBuilder {
    var userAgent = "VRChaKt (https://github.com/NephyProject/VRChaKt)"

    var endpointVersion = EndpointVersion.Release

    private var authenticationInitializer: Credentials.Builder.() -> Unit = {  }
    fun authentication(initializer: Credentials.Builder.() -> Unit) {
        authenticationInitializer = initializer
    }

    var maxRetries = 3

    private var retryInMillis = 1000L
    fun retry(interval: Long, unit: TimeUnit) {
        retryInMillis = unit.toMillis(interval)
    }

    private var dispatcherConfigBuilder: DispatcherConfig.Builder.() -> Unit = {}
    fun dispatcher(builder: DispatcherConfig.Builder.() -> Unit) {
        dispatcherConfigBuilder = builder
    }

    data class DispatcherConfig(val workingThreadsCount: Int, val connectionThreadsCount: Int?) {
        class Builder {
            var workingThreadsCount = minOf(1, Runtime.getRuntime().availableProcessors() / 2)
            var connectionThreadsCount: Int? = null

            internal fun build(): DispatcherConfig {
                return DispatcherConfig(workingThreadsCount, connectionThreadsCount)
            }
        }
    }

    private var cookieConfigBuilder: CookieConfig.Builder.() -> Unit = {}
    fun cookie(builder: CookieConfig.Builder.() -> Unit) {
        cookieConfigBuilder = builder
    }

    data class CookieConfig(val acceptCookie: Boolean, val cookies: Map<String, List<Cookie>>) {
        class Builder {
            private var acceptCookie = true
            fun discardCookie() {
                acceptCookie = false
            }

            private val cookies = mutableMapOf<String, MutableList<Cookie>>()
            fun addCookie(host: String, cookie: Cookie) {
                if (host !in cookies) {
                    cookies[host] = mutableListOf(cookie)
                } else {
                    cookies[host]!!.add(cookie)
                }
            }

            internal fun build(): SessionBuilder.CookieConfig {
                return CookieConfig(acceptCookie, cookies)
            }
        }
    }

    private var httpClientEngineFactory: HttpClientEngineFactory<*>? = null
    private var httpClientConfig: (HttpClientConfig<*>.() -> Unit)? = null
    fun <T: HttpClientEngineConfig> httpClient(engineFactory: HttpClientEngineFactory<T>, block: HttpClientConfig<T>.() -> Unit = {}) {
        httpClientEngineFactory = engineFactory
        @Suppress("UNCHECKED_CAST")
        httpClientConfig = block as HttpClientConfig<*>.() -> Unit
    }

    fun build(): Session {
        val cookieConfig = CookieConfig.Builder().apply(cookieConfigBuilder).build()
        val dispatcherConfig = DispatcherConfig.Builder().apply(dispatcherConfigBuilder).build()
        val authorizationData = Credentials.Builder().apply(authenticationInitializer).build()

        val httpClient = if (httpClientEngineFactory != null) HttpClient(httpClientEngineFactory!!) else HttpClient()
        httpClient.config {
            install(HttpPlainText) {
                defaultCharset = Charsets.UTF_8
            }

            if (cookieConfig.acceptCookie) {
                install(HttpCookies) {
                    storage = AcceptAllCookiesStorage()

                    runBlocking {
                        if (cookieConfig.cookies.isNotEmpty()) {
                            for (pair in cookieConfig.cookies) {
                                for (cookie in pair.value) {
                                    storage.addCookie(pair.key, cookie)
                                }
                            }
                        }

                        if (authorizationData.cookie != null) {
                            storage.addCookie(endpointVersion.host, Cookie("auth", authorizationData.cookie, domain = ".vrchat.net", path = "/"))
                        }
                    }
                }
            }

            if (dispatcherConfig.connectionThreadsCount != null) {
                engine {
                    threadsCount = dispatcherConfig.connectionThreadsCount
                }
            }

            httpClientConfig?.invoke(this)
        }

        val dispatcher = newFixedThreadPoolContext(dispatcherConfig.workingThreadsCount, "VRChaKt.Client")
        val logger = KotlinLogging.logger("VRChaKt.Client")
        val option = ClientOption(maxRetries, retryInMillis, endpointVersion, userAgent)

        return Session(httpClient, dispatcher, authorizationData, logger, option)
    }
}

data class ClientOption(val maxRetries: Int, val retryInMillis: Long, val endpointVersion: EndpointVersion, val userAgent: String)
