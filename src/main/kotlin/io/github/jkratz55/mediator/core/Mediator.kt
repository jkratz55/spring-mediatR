/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jkratz55.mediator.core

import java.util.concurrent.CompletableFuture

/**
 * Defines a mediator to encapsulate dispatching and publishing interaction patterns.
 *
 *
 * @author Joseph Kratz
 * @since 1.0
 */
interface Mediator {

    /**
     * Dispatches a [Request] to a single [RequestHandler] synchronously
     *
     * @param request The request to be executed
     * @return
     */
    fun <TRequest: Request<TResponse>, TResponse> dispatch(request: TRequest): TResponse

    /**
     * Dispatches a [Request] to a single [RequestHandler] to execute asynchronously on another thread
     *
     * @param request The request to be executed
     * @return
     */
    fun <TRequest: Request<TResponse>, TResponse> dispatchAsync(request: TRequest): CompletableFuture<TResponse>


    /**
     * Sends the event to all registered [EventHandler]s for the particular event synchronously.
     *
     * @param event The event to send
     */
    fun emit(event: Event)

    /**
     * Sends the event to all registered [EventHandler]s for the particular event asynchronously on another thread
     *
     * @param event The event to send
     * @return
     */
    fun emitAsync(event: Event): CompletableFuture<Void>

    /**
     * Dispatches a [Command] to a single [CommandHandler] synchronously
     *
     * @param command Command to dispatch for execution
     */
    fun dispatch(command: Command)

    /**
     * Dispatches a [Command] to a single [CommandHandler] to execute asynchronously on another thread
     */
    fun dispatchAsync(command: Command): CompletableFuture<Void>

}