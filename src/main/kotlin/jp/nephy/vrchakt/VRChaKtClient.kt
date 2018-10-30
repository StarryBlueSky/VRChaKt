package jp.nephy.vrchakt

import jp.nephy.vrchakt.core.SessionBuilder
import jp.nephy.vrchakt.endpoints.Friend
import jp.nephy.vrchakt.endpoints.FriendRequest
import jp.nephy.vrchakt.endpoints.User
import jp.nephy.vrchakt.endpoints.World
import java.io.Closeable

@Suppress("UNUSED")
class VRChaKtClient(session: SessionBuilder.() -> Unit = {  }): Closeable {
    val session = SessionBuilder().apply(session).build()

    val world = World(this)
    val user = User(this)
    val friend = Friend(this)
    val friendRequest = FriendRequest(this)

    override fun close() {
        session.close()
    }
}
