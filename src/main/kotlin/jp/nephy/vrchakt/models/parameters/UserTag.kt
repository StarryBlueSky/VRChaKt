package jp.nephy.vrchakt.models.parameters

/*
    https://vrchatapi.github.io/#/UserAPI/Tags
 */

enum class UserTag {
    SystemScriptingAccess, AdminScriptingAccess,

    SystemAvatarAccess, AdminAvatarAccess,

    SystemWorldAccess, AdminWorldAccess,

    SystemFeedbackAccess,

    SystemTrustBasic, SystemTrustIntermediate, AdminModerator,

    SystemLegend,

    SystemProbableTroll
}
