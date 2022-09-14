package io.github.susimsek.order.config

import io.github.susimsek.mscommonweb.events.TransactionEvent
import io.github.susimsek.order.consumer.TransactionEventConsumer
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration(proxyBeanMethods = false)
class ConsumerConfig(private val transactionEventConsumer: TransactionEventConsumer) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun transactionEventProcessor(): Consumer<Flux<Message<TransactionEvent>>> = Consumer { stream ->
        stream.concatMap { msg ->
            mono { transactionEventConsumer.consumeEvent(msg.payload) }
        }.onErrorContinue { e, _ ->
            logger.error(e.message, e)
        }.subscribe()
    }
}
