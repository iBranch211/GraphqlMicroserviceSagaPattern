package io.github.susimsek.payment.handler

import io.github.susimsek.mscommonweb.enums.PaymentStatus
import io.github.susimsek.mscommonweb.events.OrderPurchaseEvent
import io.github.susimsek.mscommonweb.events.PaymentEvent
import io.github.susimsek.payment.model.User
import io.github.susimsek.payment.repository.UserRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderPurchaseEventHandler(private val userRepository: UserRepository) :
    EventHandler<OrderPurchaseEvent, PaymentEvent> {

    @Transactional
    override suspend fun handleEvent(event: OrderPurchaseEvent): PaymentEvent {
        val orderPrice = event.price
        val userId = event.userId
        val paymentEvent = PaymentEvent(
            orderId = event.orderId,
            price = event.price,
            status = PaymentStatus.DECLINED
        )
        return userRepository
            .findById(userId)
            .map { user ->
                deductUserBalance(orderPrice, paymentEvent, user)
                paymentEvent
            }.awaitSingle()
    }

    private fun deductUserBalance(orderPrice: Float, paymentEvent: PaymentEvent, user: User) {
        val userBalance = user.balance.toDouble()
        if (userBalance >= orderPrice) {
            user.balance = (userBalance - orderPrice).toFloat()
            userRepository.save(user).subscribe()
            paymentEvent.status = PaymentStatus.APPROVED
        }
    }
}
