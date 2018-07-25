package jp.nephy.vrchakt.components

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.HttpPlainText

class SessionBuilder {
    private var httpClientInitializer: suspend HttpClientConfig.() -> Unit = { }
    fun httpClient(initializer: suspend HttpClientConfig.() -> Unit) {
        httpClientInitializer = initializer
    }

    private var userAgent = "VRChaKt (https://github.com/NephyProject/VRChaKt)"
    fun userAgent(text: String) {
        userAgent = text
    }

    private var endpointVersion = EndpointVersion.Release
    fun endpointVersion(version: EndpointVersion) {
        endpointVersion = version
    }

    private var authenticationInitializer: AuthenticationData.() -> Unit = {  }
    fun authentication(initializer: AuthenticationData.() -> Unit) {
        authenticationInitializer = initializer
    }

    fun build(): Session {
        val authorizationData = AuthenticationData().apply(authenticationInitializer)
        val httpClient = HttpClient(Apache) {
            install(AuthenticationHandler) {
                bind(authorizationData)
            }
            install(HttpPlainText) {
                defaultCharset = Charsets.UTF_8
            }

            httpClientInitializer()
        }

        return Session(httpClient, userAgent, endpointVersion, authorizationData)
    }
}
