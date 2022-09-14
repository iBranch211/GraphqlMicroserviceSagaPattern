package io.github.susimsek.mscommonweb.events

import java.io.Serializable

data class OrderPurchaseEvent(
    var orderId: String = "",
    var userId: String = "",
    var price: Float = 0.0F
) : Event, Serializable {

    companion object {
        private const val serialVersionUID = 1L
        private var EVENT: String = "OrderPurchase"
    }

    override fun getEvent(): String {
        return EVENT
    }
}
