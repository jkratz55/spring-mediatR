package io.jkratz.mediatr.core

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 *
 */
interface MediatR {

    /**
     *
     */
    fun <TCommand: Command<TResponse>, TResponse> execute(command: TCommand): TResponse

    /**
     *
     */
    fun <TResponse> executeAsync(command: Command<TResponse>): CompletableFuture<TResponse>

    /**
     *
     */
    fun <TResponse> executeAsync(command: Command<TResponse>, executor: Executor): CompletableFuture<TResponse>

    /**
     *
     */
    fun emit(event: Event)

    /**
     *
     */
    fun emitAsync(event: Event): CompletableFuture<Void>

    /**
     *
     */
    fun emitAsync(event: Event, executor: Executor): CompletableFuture<Void>
}