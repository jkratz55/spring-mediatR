package io.jkratz.mediatr.core

/**
 *
 */
interface Event

/**
 *
 */
interface EventHandler<in TEvent> where TEvent: Event  {

    fun handle(event: Event)
}