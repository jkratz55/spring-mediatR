package io.jkratz.mediatr.core

/**
 *
 */
interface Event

/**
 *
 */
interface EventHandler<E> {

    fun handle(event: Event)
}