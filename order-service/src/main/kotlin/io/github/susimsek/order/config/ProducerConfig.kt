package io.github.susimsek.order.config

import io.github.susimsek.mscommonweb.events.OrderPurchaseEvent
import io.github.susimsek.order.producer.OrderPurchaseProducer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Supplier

@Configuration(proxyBeanMethods = false)
class ProducerConfig(private val orderPurchaseProducer: OrderPurchaseProducer) {

    @Bean
    fun orderPurchaseEventPublisher(): Supplier<Flux<Message<OrderPurchaseEvent>>> = Supplier {
        orderPurchaseProducer.getProducer().asFlux()
    }
}
