package io.jkratz.spring.mediator

interface EventHandler<TEvent> {

    fun handle(event: TEvent)
}