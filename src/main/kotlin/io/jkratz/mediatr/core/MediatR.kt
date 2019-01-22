package io.jkratz.mediatr.core

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 *
 */
interface MediatR {

    /**
     * Executes a command
     */
    fun <TCommand: Command<TResponse>, TResponse> execute(command: TCommand): TResponse

    /**
     * Executes a command asynchronously
     */
    fun <TCommand: Command<TResponse>, TResponse> executeAsync(command: TCommand, executor: Executor? = null): CompletableFuture<TResponse>

    /**
     *
     */
    fun emit(event: Event)

    /**
     *
     */
    fun emitAsync(event: Event, executor: Executor? = null): CompletableFuture<Void>
}