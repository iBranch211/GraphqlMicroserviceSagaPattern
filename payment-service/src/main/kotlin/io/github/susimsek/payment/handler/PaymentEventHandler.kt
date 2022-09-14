package io.github.susimsek.payment.handler

import io.github.susimsek.mscommonweb.enums.PaymentStatus
import io.github.susimsek.mscommonweb.enums.TransactionStatus
import io.github.susimsek.mscommonweb.events.PaymentEvent
import io.github.susimsek.mscommonweb.events.TransactionEvent
import io.github.susimsek.payment.model.Transaction
import io.github.susimsek.payment.repository.TransactionRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentEventHandler(
    private val transactionRepository: TransactionRepository
) : EventHandler<PaymentEvent, TransactionEvent> {
    @Transactional
    override suspend fun handleEvent(event: PaymentEvent): TransactionEvent {
        return transactionRepository.save(
            Transaction(
                orderId = event.orderId,
                price = event.price
            )
        ).map {
            TransactionEvent(
            orderId = event.orderId,
            status = if (PaymentStatus.APPROVED == event.status) TransactionStatus.SUCCESSFUL
            else TransactionStatus.UNSUCCESSFUL
        )
        }
            .awaitSingle()
    }
}
