package io.github.susimsek.order.producer

import io.github.susimsek.mscommonweb.events.OrderPurchaseEvent
import io.github.susimsek.order.model.OrderPurchase
import org.springframework.integration.support.MessageBuilder
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many

@Component
class OrderPurchaseProducer {

    private val unicastProcessor = Sinks.many().unicast().onBackpressureBuffer<Message<OrderPurchaseEvent>>()

    fun getProducer(): Many<Message<OrderPurchaseEvent>> = unicastProcessor

    suspend fun produce(orderPurchase: OrderPurchase) {
        val orderPurchaseEvent = OrderPurchaseEvent(
            userId = orderPurchase.userId,
            orderId = orderPurchase.id,
            price = orderPurchase.price
        )
        val message = MessageBuilder.withPayload(orderPurchaseEvent).build()
        unicastProcessor.emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST)
    }
}
