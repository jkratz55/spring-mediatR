package io.jkratz.mediator.mock

import io.jkratz.mediator.core.Event
import io.jkratz.mediator.core.EventHandler

data class OrderCreatedEvent(val orderId: Int): Event

class OrderCreationListener: EventHandler<OrderCreatedEvent> {

    override fun handle(event: OrderCreatedEvent) {
        println("Order created with ID: ${event.orderId}")
    }
}