/*
 * Copyright 2018 the original author or authors.
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
 * API for submitting commands and events to the mediator to be routed to the
 * proper handler.
 *
 * @author Joseph Kratz
 * @since 1.0
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