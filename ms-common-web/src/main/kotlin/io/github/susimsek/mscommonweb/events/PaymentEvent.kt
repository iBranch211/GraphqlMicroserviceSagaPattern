package io.github.susimsek.mscommonweb.events

import io.github.susimsek.mscommonweb.enums.PaymentStatus
import java.io.Serializable

data class PaymentEvent(
    var orderId: String = "",
    var status: PaymentStatus = PaymentStatus.DECLINED,
    var price: Float = 0.0F
) : Event, Serializable {
    companion object {
        private const val serialVersionUID = 1L
        private var EVENT: String = "Payment"
    }

    override fun getEvent(): String {
        return EVENT
    }
}
