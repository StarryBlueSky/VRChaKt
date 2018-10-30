@file:Suppress("UNUSED")

package jp.nephy.vrchakt.models

import jp.nephy.jsonkt.ImmutableJsonObject
import jp.nephy.jsonkt.delegation.boolean
import jp.nephy.jsonkt.delegation.int
import jp.nephy.jsonkt.delegation.string
import jp.nephy.jsonkt.delegation.stringList

data class RemoteConfig(override val json: ImmutableJsonObject): VRChaKtModel {
    val address by string  // "1062 Folsom St., Suite 200, San Francisco, CA, 94103"
    val apiKey by string  // "JlE5Jldo5Jibnk5O5hTx6XVqsJu4WJ26"
    val appName by string  // "VrChat"
    val buildVersionTag by string  // "build-19-07-18-mike-wonderfulcoin"
    val clientApiKey by string  // "JlE5Jldo5Jibnk5O5hTx6XVqsJu4WJ26"
    val contactEmail by string  // "hello@vrchat.com"
    val copyrightEmail by string  // "copyright@vrchat.com"
    val currentTOSVersion by int  // 6
    val deploymentGroup by string  // "blue"
    val devAppVersionStandalone by string  // "0.12.0p3"
    val devDownloadLinkWindows by string  // "www.vrchat.net"
    val devSdkUrl by string  // "https://files.vrchat.cloud/sdk/VRCSDK-2018.06.21.13.02_Public.unitypackage"
    val devSdkVersion by string  // "2018.06.21.13.02"
    val devServerVersionStandalone by string  // "dev_server_01"
    val disableAvatarGating by boolean  // false
    val disableEventStream by boolean  // false
    val disableFeedbackGating by boolean  // true
    val downloadLinkWindows by string  // "http://d8zlo8exwu24u.cloudfront.net/stable/VRChat_Oculus_0.11.7p6.exe"
    val gearDemoRoomId by string  // "2282253502"
    val homeWorldId by string  // "wrld_a0ad5ad3-2b2c-4a77-8220-d372d299b412"
    val hubWorldId by string  // "wrld_eb7a5096-9c93-41db-a9d7-7b349a5d4815"
    val jobsEmail by string  // "jobs@vrchat.com"
    val messageOfTheDay by string  // ""
    val moderationEmail by string  // "moderation@vrchat.com"
    val moderationQueryPeriod by int  // 60
    val notAllowedToSelectAvatarInPrivateWorldMessage by string  // "For security reasons, you're not yet allowed to select avatars in private worlds or upload content. Please continue to enjoy VRChat and we'll message you when you've been unlocked. Thanks and have fun!"
    val plugin by string  // "user_from_api,no_kick_unlisted"
    val releaseAppVersionStandalone by string  // "0.12.0p3"
    val releaseSdkUrl by string  // "https://files.vrchat.cloud/sdk/VRCSDK-2018.06.21.13.02_Public.unitypackage"
    val releaseSdkVersion by string  // "2018.06.21.13.02"
    val releaseServerVersionStandalone by string  // "public_server_01"
    val sdkDeveloperFaqUrl by string  // "https://www.vrchat.com/developerfaq"
    val sdkDiscordUrl by string  // "https://discord.gg/vrchat"
    val sdkNotAllowedToPublishMessage by string  // "Welcome the VRChat SDK!\r\n\r\nBefore you can upload avatars or worlds to VRChat, you'll need to spend more time enjoying the app. We do this for security reasons, and so you can learn more about us.\r\n\r\nWhen you get the ability to upload, we will notify you via email and in VRChat. For now, you can learn and test on your own device.\r\n\r\nTo get started, check out the resources below.\r\n\r\nThank you for your patience, we can't wait to see what you'll build!"
    val sdkUnityVersion by string  // "5.6.3p1"
    val serverName by string  // "blue-api-2160"
    val supportEmail by string  // "support@vrchat.com"
    val timeOutWorldId by string  // "wrld_5b89c79e-c340-4510-be1b-476e9fcdedcc"
    val tutorialWorldId by string  // "wld_7d3d25ec-663e-406e-96a3-e2c4fc0d8104"
    val userUpdatePeriod by int  // 60
    val userVerificationDelay by int  // 6
    val userVerificationRetry by int  // 40
    val userVerificationTimeout by int  // 60
    val viveWindowsUrl by string  // "http://store.steampowered.com/app/438100/"
    val whiteListedAssetUrls by stringList  // ["http://dbinj8iahsbec.cloudfront.net/plugins", ...]
    val worldUpdatePeriod by int  // 60
}
