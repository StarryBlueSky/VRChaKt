package jp.nephy.vrchakt.core.i18n

import java.util.*

enum class LocalizedString(private val ja: String, private val en: String) {
    CredentialsAreAllNull(
            "資格情報がセットされていません。",
            "All credentials are not set yet."
    ),
    ApiRequestFailedLog(
            "APIのリクエストに失敗しました。 (%s, %d/%d)",
            "Failed to request API. (%s, %d/%d)"
    ),
    ApiRequestFailed(
            "APIのリクエストに失敗しました: %s",
            "Failed to request API: %s"
    ),
    ApiReturnedNon200StatusCode(
            "APIが2xxではないステータスコードを返却しました: %s %s",
            "API returned non-200 status code: %s %s"
    ),
    JsonParsingFailed(
            "JSONをパースする際に例外が発生しました: %s",
            "Failed to parse JSON: %s"
    ),
    JsonModelCastFailed(
            "JSONをモデルクラスにキャストする際に例外が発生しました: %s\n%s",
            "Failed to cast JSON to model class: %s\n%s"
    ),
    InvalidJsonReturned(
            "不正なJSONが返却されました。\n%s",
            "Invalid JSON returned.\n%s"
    ),
    ApiError(
            "APIエラーが発生しました。 (%d: %s)",
            "API error occurred. (%d: %s)"
    ),
    ExceptionInAsyncBlock(
            "非同期実行中にエラーが発生しました。",
            "Exception in async block."
    );

    fun format(vararg args: Any?): String {
        val locale = Locale.getDefault()
        val text = when (locale) {
            Locale.JAPANESE, Locale.JAPAN -> ja
            else -> en
        }
        return text.format(locale, *args)
    }

    override fun toString(): String {
        return format()
    }
}
