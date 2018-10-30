# VRChaKt
VRChat API wrapper for Kotlin. (WIP)

# Feature
- Null-safety. Lightweight. Fast. Written in Kotlin!
- Supports both of Blocking (Sync) and Non-blocking (Async) execution.
- Pre-made json models.
- Endpoint version switch for future api changes.

# TODO
- Support more endpoints.
- Documentize / Update README.

# Usage
```kotlin
import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.core.EndpointVersion

fun main(args: Array<String>) {
    VRChaKtClient {
        endpointVersion = EndpointVersion.Release
        userAgent = "TestClient (https://github.com/NephyProject/VRChaKt)"
        authentication {
            user("username", "password")
        }
    }.use {
        // Get The Hub world. (Blocking)
        val world = it.world.show("wrld_b51f016d-1073-4c75-930d-9f44222c7fc3").complete()
        println(world.result.description)
        
        // Get friends list. (Async)
        it.friend.list(offline = true).queue { result ->
            result.forEach { friend ->
                println(friend.displayName)
            }
        }
    }
}
```

# License
This project is provided with MIT License.
