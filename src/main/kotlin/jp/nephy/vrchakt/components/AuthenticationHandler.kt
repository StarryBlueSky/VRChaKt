package jp.nephy.vrchakt.components

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.http.HttpHeaders
import io.ktor.util.AttributeKey
import jp.nephy.vrchakt.exceptions.VRChaKtException

class AuthenticationHandler(private val authenticationData: AuthenticationData) {
    class Configuration {
        private lateinit var authenticationData: AuthenticationData
        fun bind(target: AuthenticationData) {
            authenticationData = target
        }

        fun build(): AuthenticationHandler {
            return AuthenticationHandler(authenticationData)
        }
    }

    companion object Feature : HttpClientFeature<Configuration, AuthenticationHandler> {
        override val key: AttributeKey<AuthenticationHandler> = AttributeKey("AuthenticationHandler")
        private val headerName = HttpHeaders.Authorization

        const val requiredAuthorizationFlag = "REQUIRED_AUTHORIZATION"

        override suspend fun prepare(block: Configuration.() -> Unit): AuthenticationHandler {
            return Configuration().apply(block).build()
        }

        override fun install(feature: AuthenticationHandler, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                if (context.headers.names().contains(requiredAuthorizationFlag)) {
                    if (! context.headers.names().contains(headerName)) {
                        if (feature.authenticationData.cookie == null) {
                            throw VRChaKtException { "Auth cookie is not set." }
                        }

                        context.headers.append(headerName, "Bearer ${feature.authenticationData.cookie}")
                    }

                    context.headers.remove(requiredAuthorizationFlag)
                }
            }
        }
    }
}
