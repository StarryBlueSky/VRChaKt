@file:Suppress("UNUSED")

package jp.nephy.vrchakt.core

import io.ktor.client.request.HttpRequest
import io.ktor.client.response.HttpResponse
import jp.nephy.vrchakt.core.i18n.LocalizedString
import jp.nephy.vrchakt.models.Error

open class VRChaKtException(override val message: String, val request: HttpRequest? = null, val response: HttpResponse? = null): Exception()
class VRChaKtLocalizedException(localizedString: LocalizedString, request: HttpRequest? = null, response: HttpResponse? = null, vararg args: Any?): VRChaKtException(localizedString.format(*args), request = request, response = response)

class VRChatApiError(val result: Error, request: HttpRequest, response: HttpResponse): VRChaKtException(LocalizedString.ApiError.format(result.statusCode, result.message), request, response)
