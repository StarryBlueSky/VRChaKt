package jp.nephy.vrchakt.core

import io.ktor.http.URLProtocol

enum class EndpointVersion(val protocol: URLProtocol, val host: String, vararg val paths: String) {
    Release(URLProtocol.HTTPS, "api.vrchat.cloud", "api", "1"),
    Beta(URLProtocol.HTTPS, "beta-api.vrchat.cloud", "api", "1"),
    Dev(URLProtocol.HTTPS, "dev-api.vrchat.cloud", "api", "1"),
    WebApi(URLProtocol.HTTPS, "www.vrchat.net", "api", "1")
}
