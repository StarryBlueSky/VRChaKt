package jp.nephy.vrchakt.models

import com.google.gson.JsonObject
import jp.nephy.jsonkt.byBool
import jp.nephy.jsonkt.byInt
import jp.nephy.jsonkt.byString
import jp.nephy.jsonkt.byStringList

class RemoteConfig(override val json: JsonObject): VRChaKtModel {
    val address by json.byString  // "1062 Folsom St., Suite 200, San Francisco, CA, 94103"
    val apiKey by json.byString  // "JlE5Jldo5Jibnk5O5hTx6XVqsJu4WJ26"
    val appName by json.byString  // "VrChat"
    val buildVersionTag by json.byString  // "build-19-07-18-mike-wonderfulcoin"
    val clientApiKey by json.byString  // "JlE5Jldo5Jibnk5O5hTx6XVqsJu4WJ26"
    val contactEmail by json.byString  // "hello@vrchat.com"
    val copyrightEmail by json.byString  // "copyright@vrchat.com"
    val currentTOSVersion by json.byInt  // 6
    val deploymentGroup by json.byString  // "blue"
    val devAppVersionStandalone by json.byString  // "0.12.0p3"
    val devDownloadLinkWindows by json.byString  // "www.vrchat.net"
    val devSdkUrl by json.byString  // "https://files.vrchat.cloud/sdk/VRCSDK-2018.06.21.13.02_Public.unitypackage"
    val devSdkVersion by json.byString  // "2018.06.21.13.02"
    val devServerVersionStandalone by json.byString  // "dev_server_01"
    val disableAvatarGating by json.byBool  // false
    val disableEventStream by json.byBool  // false
    val disableFeedbackGating by json.byBool  // true
    val downloadLinkWindows by json.byString  // "http://d8zlo8exwu24u.cloudfront.net/stable/VRChat_Oculus_0.11.7p6.exe"
    val gearDemoRoomId by json.byString  // "2282253502"
    val homeWorldId by json.byString  // "wrld_a0ad5ad3-2b2c-4a77-8220-d372d299b412"
    val hubWorldId by json.byString  // "wrld_eb7a5096-9c93-41db-a9d7-7b349a5d4815"
    val jobsEmail by json.byString  // "jobs@vrchat.com"
    val messageOfTheDay by json.byString  // ""
    val moderationEmail by json.byString  // "moderation@vrchat.com"
    val moderationQueryPeriod by json.byInt  // 60
    val notAllowedToSelectAvatarInPrivateWorldMessage by json.byString  // "For security reasons, you're not yet allowed to select avatars in private worlds or upload content. Please continue to enjoy VRChat and we'll message you when you've been unlocked. Thanks and have fun!"
    val plugin by json.byString  // "user_from_api,no_kick_unlisted"
    val releaseAppVersionStandalone by json.byString  // "0.12.0p3"
    val releaseSdkUrl by json.byString  // "https://files.vrchat.cloud/sdk/VRCSDK-2018.06.21.13.02_Public.unitypackage"
    val releaseSdkVersion by json.byString  // "2018.06.21.13.02"
    val releaseServerVersionStandalone by json.byString  // "public_server_01"
    val sdkDeveloperFaqUrl by json.byString  // "https://www.vrchat.com/developerfaq"
    val sdkDiscordUrl by json.byString  // "https://discord.gg/vrchat"
    val sdkNotAllowedToPublishMessage by json.byString  // "Welcome the VRChat SDK!\r\n\r\nBefore you can upload avatars or worlds to VRChat, you'll need to spend more time enjoying the app. We do this for security reasons, and so you can learn more about us.\r\n\r\nWhen you get the ability to upload, we will notify you via email and in VRChat. For now, you can learn and test on your own device.\r\n\r\nTo get started, check out the resources below.\r\n\r\nThank you for your patience, we can't wait to see what you'll build!"
    val sdkUnityVersion by json.byString  // "5.6.3p1"
    val serverName by json.byString  // "blue-api-2160"
    val supportEmail by json.byString  // "support@vrchat.com"
    val timeOutWorldId by json.byString  // "wrld_5b89c79e-c340-4510-be1b-476e9fcdedcc"
    val tutorialWorldId by json.byString  // "wld_7d3d25ec-663e-406e-96a3-e2c4fc0d8104"
    val userUpdatePeriod by json.byInt  // 60
    val userVerificationDelay by json.byInt  // 6
    val userVerificationRetry by json.byInt  // 40
    val userVerificationTimeout by json.byInt  // 60
    val viveWindowsUrl by json.byString  // "http://store.steampowered.com/app/438100/"
    val whiteListedAssetUrls by json.byStringList  // ["http://dbinj8iahsbec.cloudfront.net/plugins", ...]
    val worldUpdatePeriod by json.byInt  // 60
}
