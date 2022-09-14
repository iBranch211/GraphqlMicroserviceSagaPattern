package io.github.susimsek.payment.config

import io.github.susimsek.mscommonweb.events.OrderPurchaseEvent
import io.github.susimsek.mscommonweb.events.PaymentEvent
import io.github.susimsek.mscommonweb.events.TransactionEvent
import io.github.susimsek.payment.handler.OrderPurchaseEventHandler
import io.github.susimsek.payment.handler.PaymentEventHandler
import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import reactor.core.publisher.Flux
import java.util.function.Function

@Configuration(proxyBeanMethods = false)
class ConsumerConfig(
    private val orderPurchaseEventHandler: OrderPurchaseEventHandler,
    private val paymentEventHandler: PaymentEventHandler
    ) {

    @Bean
    fun orderPurchaseEventProcessor(): Function<Flux<Message<OrderPurchaseEvent>>, Flux<Message<PaymentEvent>>> =
        Function { stream ->
            stream.concatMap {
                mono { orderPurchaseEventHandler.handleEvent(it.payload) }
            }.map {
                MessageBuilder.withPayload(it).build()
            }
        }

    @Bean
    fun paymentEventSubscriber(): Function<Flux<Message<PaymentEvent>>, Flux<Message<TransactionEvent>>> =
        Function { stream ->
        stream.concatMap {
            mono { paymentEventHandler.handleEvent(it.payload) }
        }.map {
            MessageBuilder.withPayload(it).build()
        }
    }
}
