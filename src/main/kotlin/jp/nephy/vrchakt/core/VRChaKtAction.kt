@file:Suppress("UNUSED")

package jp.nephy.vrchakt.core

import io.ktor.client.request.HttpRequest
import io.ktor.client.request.request
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.isSuccess
import io.ktor.util.flattenEntries
import jp.nephy.jsonkt.*
import jp.nephy.vrchakt.core.i18n.LocalizedString
import jp.nephy.vrchakt.models.Empty
import jp.nephy.vrchakt.models.Error
import jp.nephy.vrchakt.models.VRChaKtModel
import kotlinx.coroutines.*
import mu.KotlinLogging
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger("VRChaKt.ApiAction")

interface VRChaKtAction {
    val request: VRChaKtRequest
}

private interface JsonRequest<M: VRChaKtModel> {
    val model: KClass<M>
}

private typealias ApiCallback<R> = (response: R) -> Unit
private typealias ApiFallback = (e: Throwable) -> Unit

abstract class ApiAction<R>(private val dispatcher: CoroutineDispatcher) {
    private val defaultCallback: ApiCallback<R> = { }
    private val defaultFallback: ApiFallback = { e ->
        logger.error(e) { LocalizedString.ExceptionInAsyncBlock.format() }
    }

    @Throws(VRChaKtException::class, CancellationException::class)
    abstract suspend fun await(): R

    @Throws(VRChaKtException::class, CancellationException::class)
    suspend fun awaitWithTimeout(timeout: Long, unit: TimeUnit): R? {
        return withTimeoutOrNull(unit.toMillis(timeout)) {
            await()
        }
    }

    @Throws(VRChaKtException::class)
    fun complete(context: CoroutineContext? = null): R {
        return runBlocking(context ?: dispatcher) {
            await()
        }
    }

    @Throws(VRChaKtException::class)
    fun completeWithTimeout(timeout: Long, unit: TimeUnit, context: CoroutineContext? = null): R? {
        return runBlocking(context ?: dispatcher) {
            awaitWithTimeout(timeout, unit)
        }
    }

    fun queue(scope: CoroutineScope = GlobalScope, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onSuccess: ApiCallback<R>, onFailure: ApiFallback): Job {
        return scope.launch(context ?: dispatcher, start) {
            try {
                await().let(onSuccess)
            } catch (e: Exception) {
                try {
                    onFailure(e)
                } catch (e: Exception) {
                    defaultFallback(e)
                }
            }
        }
    }

    fun queue(scope: CoroutineScope = GlobalScope, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onSuccess: ApiCallback<R>): Job {
        return queue(scope, context, start, onSuccess, defaultFallback)
    }

    fun queue(scope: CoroutineScope = GlobalScope, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT): Job {
        return queue(scope, context, start, defaultCallback, defaultFallback)
    }

    fun queueWithTimeout(timeout: Long, unit: TimeUnit, scope: CoroutineScope = GlobalScope, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onSuccess: ApiCallback<R>, onFailure: ApiFallback): Job {
        return scope.launch(context ?: dispatcher, start) {
            try {
                withTimeout(unit.toMillis(timeout)) {
                    await()
                }.let(onSuccess)
            } catch (e: Exception) {
                try {
                    onFailure(e)
                } catch (e: Exception) {
                    defaultFallback(e)
                }
            }
        }
    }

    fun queueWithTimeout(timeout: Long, unit: TimeUnit, scope: CoroutineScope = GlobalScope, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onSuccess: ApiCallback<R>): Job {
        return queueWithTimeout(timeout, unit, scope, context, start, onSuccess, defaultFallback)
    }

    fun queueWithTimeout(timeout: Long, unit: TimeUnit, scope: CoroutineScope = GlobalScope, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT): Job {
        return queueWithTimeout(timeout, unit, scope, context, start, defaultCallback, defaultFallback)
    }
}

private suspend fun executeRequest(session: Session, request: VRChaKtRequest): Pair<HttpRequest, HttpResponse> {
    repeat(session.option.maxRetries) {
        try {
            val response = session.httpClient.request<HttpResponse>(request.builder.finalize())
            return response.call.request to response
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // TEMP FIX: Set-CookieConfig header format may be invalid like Sat, 5 Sep 2020 16:30:05 GMT
            if (e is IllegalStateException && e.message.orEmpty().startsWith("Invalid date length.")) {
                logger.debug(e) { LocalizedString.ApiRequestFailedLog.format(request.builder.url, it + 1, session.option.maxRetries) }
            } else {
                logger.error(e) { LocalizedString.ApiRequestFailedLog.format(request.builder.url, it + 1, session.option.maxRetries) }
            }
        }

        if (it < session.option.maxRetries) {
            delay(session.option.retryInMillis)
        }
    }

    throw VRChaKtLocalizedException(LocalizedString.ApiRequestFailed, args = *arrayOf(request.builder.url))
}

private suspend fun HttpResponse.readTextSafe(): String? {
    val maxRetries = 3
    repeat(maxRetries) {
        try {
            return readText().trim().unescapeHTML()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.error(e) { "Failed to read text. (${it + 1}/$maxRetries)\n${call.request.url}" }
        }
    }

    return null
}

internal fun String.unescapeHTML(): String {
    return replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">")
}

private fun String.toJsonObjectSafe(): JsonObject? {
    return try {
        toJsonObject()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logger.error(e) { LocalizedString.JsonParsingFailed.format(this) }
        null
    }
}

private fun String.toJsonArraySafe(): JsonArray? {
    return try {
        toJsonArray()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logger.error(e) { LocalizedString.JsonParsingFailed.format(this) }
        null
    }
}

private fun <T: VRChaKtModel> JsonObject.parseSafe(model: KClass<T>, content: String?): T? {
    return try {
        parse(model)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logger.error(e) { LocalizedString.JsonModelCastFailed.format(model.simpleName, content) }
        null
    }
}

private fun <T: VRChaKtModel> JsonArray.parseSafe(model: KClass<T>, content: String?): List<T> {
    return try {
        parseList(model)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logger.error(e) { LocalizedString.JsonModelCastFailed.format(model.simpleName, content) }
        emptyList()
    }
}

private fun checkError(request: HttpRequest, response: HttpResponse, content: String?) {
    if (content == null) {
        return
    }

    logger.trace {
        buildString {
            appendln("${response.version} ${response.status.value} ${request.method.value} ${request.url}")

            val (requestHeaders, responseHeaders) = request.headers.flattenEntries() to response.headers.flattenEntries()
            val (longestRequestHeaderLength, longestResponseHeaderLength) = requestHeaders.maxBy { it.first.length }?.first.orEmpty().length + 1 to responseHeaders.maxBy { it.first.length }?.first.orEmpty().length + 1
            appendln("Request headers =\n${requestHeaders.joinToString("\n") { "    ${it.first.padEnd(longestRequestHeaderLength)}: ${it.second}" }}")
            appendln("Response headers =\n${responseHeaders.joinToString("\n") { "    ${it.first.padEnd(longestResponseHeaderLength)}: ${it.second}" }}\n")

            append(
                    when {
                        content.isBlank() -> {
                            "(Empty Response)"
                        }
                        else -> {
                            content
                        }
                    }
            )
        }
    }

    if (response.status.isSuccess()) {
        return
    }

    val json = content.toJsonObjectSafe() ?: return
    if ("error" in json) {
        val result = json.parse<Error.Result>()
        throw VRChatApiError(result.error, request, response)
    }

    throw VRChaKtLocalizedException(LocalizedString.ApiReturnedNon200StatusCode, request, response, response.status.value, response.status.description)
}

class VRChaKtJsonObjectAction<M: VRChaKtModel>(override val request: VRChaKtRequest, override val model: KClass<M>): VRChaKtAction, JsonRequest<M>, ApiAction<VRChaKtJsonObjectResponse<M>>(request.session.dispatcher) {
    override suspend fun await(): VRChaKtJsonObjectResponse<M> {
        val (request, response) = executeRequest(request.session, request)
        val content = response.readTextSafe()
        checkError(request, response, content)

        val json = content?.toJsonObjectSafe() ?: if (model == Empty::class.java) {
            jsonObjectOf()
        } else {
            throw VRChaKtLocalizedException(LocalizedString.JsonParsingFailed, request, response, content)
        }

        val result = json.parseSafe(model, content) ?: throw VRChaKtLocalizedException(LocalizedString.JsonModelCastFailed, args = *arrayOf(model.simpleName, content))

        return VRChaKtJsonObjectResponse(model, result, request, response, content.orEmpty(), this)
    }
}

class VRChaKtJsonArrayAction<M: VRChaKtModel>(override val request: VRChaKtRequest, override val model: KClass<M>): VRChaKtAction, JsonRequest<M>, ApiAction<VRChaKtJsonArrayResponse<M>>(request.session.dispatcher) {
    override suspend fun await(): VRChaKtJsonArrayResponse<M> {
        val (request, response) = executeRequest(request.session, request)
        val content = response.readTextSafe()
        checkError(request, response, content)

        val json = content?.toJsonArraySafe() ?: throw VRChaKtLocalizedException(LocalizedString.JsonParsingFailed, request, response, content)

        return VRChaKtJsonArrayResponse(model, request, response, content, this).apply {
            addAll(json.parseSafe(model, content))
        }
    }
}

class VRChaKtTextAction(override val request: VRChaKtRequest): VRChaKtAction, ApiAction<VRChaKtTextResponse>(request.session.dispatcher) {
    override suspend fun await(): VRChaKtTextResponse {
        val (request, response) = executeRequest(request.session, request)
        val content = response.readTextSafe()
        checkError(request, response, content)

        return VRChaKtTextResponse(request, response, content.orEmpty(), this)
    }
}

private typealias JsonObjectActionCallback<M> = (results: VRChaKtMultipleJsonObjectActions.Results<M>) -> VRChaKtJsonObjectAction<*>

class VRChaKtMultipleJsonObjectActions<M: VRChaKtModel>(val first: VRChaKtJsonObjectAction<M>, private val requests: List<JsonObjectActionCallback<M>>): ApiAction<List<VRChaKtJsonObjectResponse<*>>>(first.request.session.dispatcher) {
    class Builder<M: VRChaKtModel>(private val first: () -> VRChaKtJsonObjectAction<M>) {
        private val requests = mutableListOf<JsonObjectActionCallback<M>>()
        fun request(callback: JsonObjectActionCallback<M>) = apply {
            requests.add(callback)
        }

        internal fun build(): VRChaKtMultipleJsonObjectActions<M> {
            return VRChaKtMultipleJsonObjectActions(first(), requests)
        }
    }

    class Results<M: VRChaKtModel>(val first: VRChaKtJsonObjectResponse<M>, val responses: Map<KClass<out VRChaKtModel>, List<VRChaKtJsonObjectResponse<*>>>) {
        inline fun <reified T: VRChaKtModel> responses(): List<VRChaKtJsonObjectResponse<T>> {
            @Suppress("UNCHECKED_CAST")
            return responses[T::class].orEmpty().map { it as VRChaKtJsonObjectResponse<T> }
        }
    }

    override suspend fun await(): List<VRChaKtJsonObjectResponse<*>> {
        val first = first.await()
        val responses = mutableMapOf<KClass<out VRChaKtModel>, MutableList<VRChaKtJsonObjectResponse<*>>>()
        val responsesList = mutableListOf<VRChaKtJsonObjectResponse<*>>()
        for (request in requests) {
            val results = Results(first, responses)
            val result = request.invoke(results).await()

            if (result.model in responses) {
                responses[result.model]!!.add(result)
            } else {
                responses[result.model] = mutableListOf(result)
            }
            responsesList += result
        }

        return responsesList
    }

    operator fun plus(callback: JsonObjectActionCallback<M>): VRChaKtMultipleJsonObjectActions<M> {
        return Builder {
            first
        }.also { builder ->
            requests.forEach {
                builder.request(it)
            }
        }.request(callback).build()
    }
}

private typealias JoinedJsonObjectActionCallback<M> = (results: List<List<VRChaKtJsonObjectResponse<*>>>) -> VRChaKtJsonObjectAction<M>

class VRChaKtJoinedJsonObjectActions<M: VRChaKtModel, T: VRChaKtModel>(private val actions: List<VRChaKtMultipleJsonObjectActions<M>>, private val finalizer: JoinedJsonObjectActionCallback<T>): ApiAction<VRChaKtJsonObjectResponse<T>>(actions.first().first.request.session.dispatcher) {
    override suspend fun await(): VRChaKtJsonObjectResponse<T> {
        return finalizer(actions.map { it.await() }).await()
    }
}

fun <M: VRChaKtModel, T: VRChaKtModel> List<VRChaKtMultipleJsonObjectActions<M>>.join(finalizer: JoinedJsonObjectActionCallback<T>): VRChaKtJoinedJsonObjectActions<M, T> {
    return VRChaKtJoinedJsonObjectActions(this, finalizer)
}

inline fun <reified M: VRChaKtModel> List<VRChaKtJsonObjectResponse<*>>.filter(): List<VRChaKtJsonObjectResponse<M>> {
    @Suppress("UNCHECKED_CAST")
    return filter { it.model == M::class.java }.map { it as VRChaKtJsonObjectResponse<M> }
}
