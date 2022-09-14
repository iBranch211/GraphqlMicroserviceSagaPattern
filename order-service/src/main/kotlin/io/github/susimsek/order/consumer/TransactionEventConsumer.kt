package io.github.susimsek.order.consumer

import io.github.susimsek.mscommonweb.enums.OrderStatus
import io.github.susimsek.mscommonweb.enums.TransactionStatus
import io.github.susimsek.mscommonweb.events.TransactionEvent
import io.github.susimsek.order.model.OrderPurchase
import io.github.susimsek.order.repository.OrderPurchaseRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component

@Component
class TransactionEventConsumer(
    private val orderPurchaseRepository: OrderPurchaseRepository
) : EventConsumer<TransactionEvent> {

    override suspend fun consumeEvent(event: TransactionEvent) {
        val order = orderPurchaseRepository.findById(event.orderId).awaitSingleOrNull()

        if (order != null) {
            setStatus(event, order)
            orderPurchaseRepository.save(order).subscribe()
        }
    }

    private fun setStatus(transactionEvent: TransactionEvent, order: OrderPurchase) {
        order.status =
            if (TransactionStatus.SUCCESSFUL == transactionEvent.status) OrderStatus.COMPLETED else OrderStatus.FAILED
    }
}
