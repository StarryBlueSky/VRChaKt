package jp.nephy.vrchakt.components

import io.ktor.http.URLProtocol

enum class EndpointVersion(val protocol: URLProtocol, val host: String, val path: Array<String>) {
    Release(URLProtocol.HTTPS, "api.vrchat.cloud", arrayOf("api", "1")),
    Beta(URLProtocol.HTTPS, "beta-api.vrchat.cloud", arrayOf("api", "1")),
    Dev(URLProtocol.HTTPS, "dev-api.vrchat.cloud", arrayOf("api", "1"))
}
