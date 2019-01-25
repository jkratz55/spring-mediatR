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

package io.jkratz.mediatr.core

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * Defines a mediator to encapsulate dispatching and publishing interaction patterns.
 *
 * Note: While written in Kotlin because Java does not have default parameters additional
 * overloads were added for better interoperability.
 *
 * @author Joseph Kratz
 * @since 1.0
 */
interface Mediator {

    /**
     * Dispatches a request to a single RequestHandler
     *
     * @param request The request to be executed
     * @return
     */
    fun <TRequest: Request<TResponse>, TResponse> dispatch(request: TRequest): TResponse

    /**
     * Dispatches a request to a single RequestHandler which will execute on a separate Thread,
     * using the default ForkJoinPool
     *
     * @param request The request to be executed
     * @return
     */
    fun <TRequest: Request<TResponse>, TResponse> dispatchAsync(request: TRequest): CompletableFuture<TResponse>

    /**
     * Dispatches a request to a single RequestHandler which will execute on a separate Thread,
     * using the provided [Executor]. If pass NULL for the executor the default ForkJoinPool will
     * be used.
     *
     * @param request The request to be executed
     * @param executor The executor to execute the request
     * @return
     */
    fun <TRequest: Request<TResponse>, TResponse> dispatchAsync(request: TRequest, executor: Executor): CompletableFuture<TResponse>

    /**
     * Sends the event to all registered handlers for the particular event.
     *
     * @param event The event to send
     */
    fun emit(event: Event)

    /**
     * Sends the event to all registered handlers for the particular event using a separate Thread.
     * The default ForkJoinPool will be used.
     *
     * @param event The event to send
     * @return
     */
    fun emitAsync(event: Event): CompletableFuture<Void>

    /**
     * Sends the event to all registered handlers for the particular event using a separate Thread.
     * The executor is used to execute the [EventHandler], the default ForkJoinPool will be used
     * if executor is NULL.
     *
     * @param event The event to send
     * @param executor The executor to execute the request
     * @return
     */
    fun emitAsync(event: Event, executor: Executor): CompletableFuture<Void>
}