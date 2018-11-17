@file:Suppress("UNUSED")

package jp.nephy.vrchakt.core

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.client.utils.EmptyContent
import io.ktor.http.*
import io.ktor.http.content.OutgoingContent
import io.ktor.util.appendAll
import io.ktor.util.flattenEntries
import io.ktor.util.flattenForEach
import jp.nephy.jsonkt.JsonObject
import jp.nephy.jsonkt.asJsonElement
import jp.nephy.jsonkt.toJsonString
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.writeFully
import kotlinx.coroutines.io.writeStringUtf8
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.json
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger("VRChaKt.RequestBuilder")

class VRChaKtRequestBuilder(private val session: Session, private val httpMethod: HttpMethod, private val path: String) {
    private var headers = HeadersBuilder()
    private val parameters = ParametersBuilder()

    init {
        header(HttpHeaders.UserAgent, session.option.userAgent)
        if (session.credentials.basic != null) {
            header(HttpHeaders.Authorization, "Basic ${session.credentials.basic}")
        }
        parameter("apiKey", session.apiKey)
    }

    fun header(key: String, value: Any?) {
        headers[key] = value?.toString() ?: return
    }

    fun header(vararg pairs: Pair<String, Any?>) {
        for (pair in pairs) {
            header(pair.first, pair.second)
        }
    }

    fun header(headers: Headers) {
        headers.flattenForEach { key, value ->
            header(key, value)
        }
    }

    fun parameter(key: String, value: Any?) {
        parameters[key] = value?.toString() ?: return
    }

    fun parameter(vararg pairs: Pair<String, Any?>) {
        for (pair in pairs) {
            parameter(pair.first, pair.second)
        }
    }

    private var body: Any = EmptyContent
    fun body(builder: RequestBodyBuilder.() -> Unit) {
        body = RequestBodyBuilder().apply(builder).build()
    }

    val url: String
        get() = URLBuilder(protocol = session.option.endpointVersion.protocol, host = session.option.endpointVersion.host, port = session.option.endpointVersion.protocol.defaultPort, encodedPath = "/${session.option.endpointVersion.paths.joinToString("/")}$path", parameters = parameters.copy()).buildString()

    internal fun finalize(): (HttpRequestBuilder) -> Unit {
        return {
            it.method = httpMethod
            it.url(url)
            it.headers.appendAll(headers)
            it.body = body
        }
    }

    internal fun build(): VRChaKtRequest {
        return VRChaKtRequest(session, this)
    }
}

internal fun ParametersBuilder.copy(): ParametersBuilder {
    return ParametersBuilder().apply { appendAll(this@copy) }
}

class RequestBodyBuilder {
    private var encodedForm: EncodedFormContent? = null
    fun form(builder: EncodedFormContent.Builder.() -> Unit) {
        encodedForm = EncodedFormContent.Builder().apply(builder).build()
    }

    private var multiPart: MultiPartContent? = null
    fun multiPart(builder: MultiPartContent.Builder.() -> Unit) {
        multiPart = MultiPartContent.Builder().apply(builder).build()
    }

    private var json: JsonTextContent? = null
    fun json(builder: JsonTextContent.Builder.() -> Unit) {
        json = JsonTextContent.Builder().apply(builder).build()
    }

    internal fun build(): Any {
        return encodedForm ?: multiPart ?: json ?: EmptyContent
    }
}

class EncodedFormContent(val forms: Parameters): OutgoingContent.WriteChannelContent() {
    override val contentType = ContentType.Application.FormUrlEncoded.withCharset(Charsets.UTF_8)

    override suspend fun writeTo(channel: ByteWriteChannel) {
        channel.writeStringUtf8(forms.formUrlEncode())
    }

    class Builder {
        private val forms = ParametersBuilder()

        fun add(key: String, value: Any?) {
            forms[key] = value?.toString() ?: return
        }

        fun add(vararg pairs: Pair<String, Any?>) {
            for (pair in pairs) {
                add(pair.first, pair.second)
            }
        }

        internal fun build(): EncodedFormContent {
            return EncodedFormContent(forms.build())
        }
    }
}

// From https://github.com/ktorio/ktor-samples/blob/183dd65e39565d6d09682a9b273937013d2124cc/other/client-multipart/src/MultipartApp.kt#L57
class MultiPartContent(private val parts: List<Part>): OutgoingContent.WriteChannelContent() {

    private val boundary = "***VRChaKt-${UUID.randomUUID()}-VRChaKt-${System.currentTimeMillis()}***"
    override val contentType = ContentType.MultiPart.FormData.withParameter("boundary", boundary).withCharset(Charsets.UTF_8)

    override suspend fun writeTo(channel: ByteWriteChannel) {
        for (part in parts) {
            channel.writeStringUtf8("--$boundary\r\n")
            val partHeaders = Headers.build {
                if (part.filename != null) {
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"${part.name}\"; filename=\"${part.filename}\"")
                } else {
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"${part.name}\"")
                }
                appendAll(part.headers)
            }
            for ((key, value) in partHeaders.flattenEntries()) {
                channel.writeStringUtf8("$key: $value\r\n")
            }
            channel.writeStringUtf8("\r\n")
            part.writer(channel)
            channel.writeStringUtf8("\r\n")
        }
        channel.writeStringUtf8("--$boundary--\r\n")
    }

    data class Part(val name: String, val filename: String?, val headers: Headers = Headers.Empty, val writer: suspend ByteWriteChannel.() -> Unit)

    class Builder {
        private val parts = arrayListOf<Part>()

        private fun add(part: Part) {
            parts += part
        }

        fun add(name: String, filename: String, contentType: ContentType, headers: Headers = Headers.Empty, writer: suspend ByteWriteChannel.() -> Unit) {
            add(Part(name, filename, headers + headersOf(HttpHeaders.ContentType, contentType.toString()), writer))
        }

        fun add(name: String, filename: String, contentType: ContentType, data: ByteArray, headers: Headers = Headers.Empty) {
            add(name, filename, contentType, headers) {
                writeFully(data)
            }
        }

        fun add(name: String, text: String, contentType: ContentType? = null) {
            add(Part(name, null, headersOf(HttpHeaders.ContentType, contentType.toString())) { writeStringUtf8(text) })
        }

        fun add(vararg pairs: Pair<String, Any?>) {
            for (pair in pairs) {
                add(pair.first, pair.second?.toString() ?: continue)
            }
        }

        internal fun build(): MultiPartContent {
            return MultiPartContent(parts.toList())
        }
    }
}

class JsonTextContent(private val json: JsonObject): OutgoingContent.WriteChannelContent() {
    override val contentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)

    override suspend fun writeTo(channel: ByteWriteChannel) {
        channel.writeStringUtf8(json.toJsonString())
    }

    class Builder {
        private val updates = mutableListOf<JsonBuilder.() -> Unit>()

        fun add(key: String, value: Any?) {
            updates += {
                key to value.asJsonElement()
            }
        }

        fun add(vararg pairs: Pair<String, Any?>) {
            for ((first, second) in pairs) {
                add(first, second)
            }
        }

        internal fun build(): JsonTextContent {
            return JsonTextContent(json {
                for (update in updates) {
                    update()
                }
            })
        }
    }
}

internal operator fun Headers.plus(other: Headers): Headers {
    return when {
        isEmpty() -> other
        other.isEmpty() -> this
        else -> Headers.build {
            appendAll(this@plus)
            appendAll(other)
        }
    }
}
