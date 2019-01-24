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

package io.jkratz.mediatr.spring

import io.jkratz.mediatr.core.*
import org.springframework.context.ApplicationContext
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import io.jkratz.mediatr.core.Request
import java.util.function.Supplier
import javax.validation.Valid

/**
 * Spring specific implementation of [MediatR]
 *
 * @author Joseph Kratz
 * @since 1.0
 */
class SpringMediatR constructor(applicationContext: ApplicationContext): MediatR {

    private val registry: Registry = Registry(applicationContext)

    override fun <TRequest: Request<TResponse>, TResponse> dispatch(@Valid request: TRequest): TResponse {
        val commandHandler = registry.get(request::class.java)
        return commandHandler.handle(request)
    }

    override fun <TRequest : Request<TResponse>, TResponse> dispatchAsync(@Valid request: TRequest, executor: Executor?): CompletableFuture<TResponse> {
        val commandHandler = registry.get(request::class.java)
        executor?.let { exec ->
            return CompletableFuture.supplyAsync(Supplier {
                commandHandler.handle(request)
            }, exec)
        } ?:
        return CompletableFuture.supplyAsync {
            commandHandler.handle(request)
        }
    }

    override fun emit(@Valid event: Event) {
        val eventHandlers = registry.get(event::class.java)
        eventHandlers.forEach { handler -> handler.handle(event) }
    }

    override fun emitAsync(@Valid event: Event, executor: Executor?): CompletableFuture<Void> {
        val eventHandlers = registry.get(event::class.java)
        return CompletableFuture.runAsync {
            eventHandlers.forEach { handler -> handler.handle(event) }
        }
    }
}