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
- Support more endpoints.
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
        // Get The Hub world.
        val world = world.getById("wrld_b51f016d-1073-4c75-930d-9f44222c7fc3").complete()

        println(world.result.description) // Print world description.
    }
}
```

# License
This project is provided with MIT License.
