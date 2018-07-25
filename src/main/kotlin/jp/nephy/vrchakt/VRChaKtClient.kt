package jp.nephy.vrchakt

import jp.nephy.vrchakt.components.SessionBuilder
import jp.nephy.vrchakt.endpoints.World
import java.io.Closeable

class VRChaKtClient(session: SessionBuilder.() -> Unit = {  }): Closeable {
    val session = SessionBuilder().apply(session).build()

    val world = World(this)

    fun with(operation: (VRChaKtClient).() -> Unit) = apply(operation).close()

    override fun close() {
        session.close()
    }
}
