package io.github.jkratz55.mediator.mock

import io.github.jkratz55.mediator.core.Event
import io.github.jkratz55.mediator.core.EventHandler

data class OrderCreatedEvent(val orderId: Int): Event

class OrderCreationListener: EventHandler<OrderCreatedEvent> {

    override fun handle(event: OrderCreatedEvent) {
        println("Order created with ID: ${event.orderId}")
    }
}