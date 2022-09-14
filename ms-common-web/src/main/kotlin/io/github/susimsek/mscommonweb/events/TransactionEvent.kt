package io.github.susimsek.mscommonweb.events

import io.github.susimsek.mscommonweb.enums.TransactionStatus
import java.io.Serializable

data class TransactionEvent(
    var orderId: String = "",
    var status: TransactionStatus = TransactionStatus.UNSUCCESSFUL
) : Event, Serializable {
    companion object {
        private const val serialVersionUID = 1L
        private var EVENT: String = "Transaction"
    }

    override fun getEvent(): String {
        return EVENT
    }
}
