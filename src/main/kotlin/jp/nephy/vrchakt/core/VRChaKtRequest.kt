package jp.nephy.vrchakt.core

import jp.nephy.vrchakt.models.Empty
import jp.nephy.vrchakt.models.VRChaKtModel

class VRChaKtRequest(val session: Session, val builder: VRChaKtRequestBuilder) {
    inline fun <reified M: VRChaKtModel> jsonObject(): VRChaKtJsonObjectAction<M> {
        return VRChaKtJsonObjectAction(this, M::class.java)
    }

    fun empty(): VRChaKtJsonObjectAction<Empty> {
        return VRChaKtJsonObjectAction(this, Empty::class.java)
    }

    inline fun <reified M: VRChaKtModel> jsonArray(): VRChaKtJsonArrayAction<M> {
        return VRChaKtJsonArrayAction(this, M::class.java)
    }

    fun text(): VRChaKtTextAction {
        return VRChaKtTextAction(this)
    }
}
