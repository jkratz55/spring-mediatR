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
 * @author Joseph Kratz
 * @since 1.0
 */
interface MediatR {

    /**
     * Dispatches a request to a single RequestHandler
     *
     * @param request The request to be executed
     * @return
     */
    fun <TRequest: Request<TResponse>, TResponse> dispatch(request: TRequest): TResponse

    /**
     * Dispatches a request to a single RequestHandler. The RequestHandler executes on another Thread
     * asynchronously and returns a [CompletableFuture]. If an [Executor] is provided it will be used
     * to execute the RequestHandler, otherwise the default ForkJoinPool will be used.
     *
     * @param request The request to be executed
     * @param executor The executor to execute the request
     * @return
     */
    fun <TRequest: Request<TResponse>, TResponse> dispatchAsync(request: TRequest, executor: Executor? = null): CompletableFuture<TResponse>

    /**
     *
     */
    fun emit(event: Event)

    /**
     *
     */
    fun emitAsync(event: Event, executor: Executor? = null): CompletableFuture<Void>
}