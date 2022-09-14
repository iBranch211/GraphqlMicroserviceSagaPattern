package io.github.susimsek.order.consumer

import io.github.susimsek.mscommonweb.events.Event

interface EventConsumer<T : Event> {
    suspend fun consumeEvent(event: T)
}
