@file:Suppress("UNUSED")

package jp.nephy.vrchakt.core

import jp.nephy.vrchakt.models.Empty
import jp.nephy.vrchakt.models.VRChaKtModel

class VRChaKtRequest(val session: Session, val builder: VRChaKtRequestBuilder) {
    inline fun <reified M: VRChaKtModel> jsonObject(): VRChaKtJsonObjectAction<M> {
        return VRChaKtJsonObjectAction(this, M::class)
    }

    fun empty(): VRChaKtJsonObjectAction<Empty> {
        return VRChaKtJsonObjectAction(this, Empty::class)
    }

    inline fun <reified M: VRChaKtModel> jsonArray(): VRChaKtJsonArrayAction<M> {
        return VRChaKtJsonArrayAction(this, M::class)
    }

    fun text(): VRChaKtTextAction {
        return VRChaKtTextAction(this)
    }
}
