package io.github.susimsek.payment.handler

import io.github.susimsek.mscommonweb.events.Event

interface EventHandler<T : Event, R : Event> {
    suspend fun handleEvent(event: T): R
}
