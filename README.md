# VRChaKt
VRChat API wrapper for Kotlin. (WIP)

# Feature
- Null-safety. Lightweight. Fast. Written in Kotlin!
- Supports both of Blocking (Sync) and Non-blocking (Async) execution.
- Pre-made json model.
- Endpoint version switch for future api changes.

# TODO
- Automate Cookie fetching.
- Implement User-login.
- Support more endpoints. (Currently supports world#getById only.)
- Documentize / Update README.
- Support i18n(internationalization). Localize exception message & README.
- Sync with Maven Central.

# Usage
```kotlin
import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.components.EndpointVersion

fun main(args: Array<String>) {
    VRChaKtClient {
        endpointVersion(EndpointVersion.Release)
        userAgent("TestClient (https://github.com/NephyProject/VRChaKt)")
        authentication {
            cookie("YOUR_AUTHCOOKIE_HERE")
        }
    }.with {
        val world = world.getById("wrld_xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx").complete()

        println(world.result.name) // Print world name.
    }
}
```

# License
This project is provided with MIT License.
