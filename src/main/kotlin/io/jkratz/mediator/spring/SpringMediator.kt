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

package io.jkratz.mediator.spring

import io.jkratz.mediator.core.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.validation.Valid

/**
 * Implementation of Mediator that is specific for the Spring Framework. This class requires it be
 * instantiated with the [ApplicationContext] containing the beans for all the handlers. The
 * [ApplicationContext] is used to retrieve all the beans that implement [CommandHandler],
 * [EventHandler], and [RequestHandler]. Optionally this class can be instantiated with a
 * [Executor]. If one if not provided a FixedThreadPool will be used with a thread count
 * equal to the count of processor available. The [Executor] is only used on the async variants
 * of the dispatch and emit events.
 *
 * @author Joseph Kratz
 * @since 1.0
 * @constructor Creates the Spring specific implementation of MediatR using the default [Executor]
 *              which is a fixed thread pool with the amount of threads equal to the number of
 *              processors available.
 * @param applicationContext Spring application context containing the beans for MediatR
 */
class SpringMediator constructor(private val registry: Registry): Mediator {

    private var executor: Executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), MediatorThreadFactory())

    /**
     * Creates the Spring specific implementation of MediatR with a custom [Executor] for
     * performing async operations.
     *
     * @param applicationContext Spring application context containing the beans for MediatR
     * @param executor The executor to execute asynchronous operations
     */
    constructor(registry: Registry, executor: Executor) : this(registry) {
        this.executor = executor
        logger.info("${executor::class.java.simpleName} will be used for asynchronous operations instead of the default Executor")
    }

    override fun <TRequest: Request<TResponse>, TResponse> dispatch(@Valid request: TRequest): TResponse {
        val handler = registry.get(request::class.java)
        logger.debug("Dispatching ${request::class.simpleName} to handler ${handler::class.simpleName}")
        return handler.handle(request)
    }

    override fun <TRequest : Request<TResponse>, TResponse> dispatchAsync(@Valid request: TRequest): CompletableFuture<TResponse> {
        return CompletableFuture.supplyAsync({
            val handler = registry.get(request::class.java)
            logger.debug("Dispatching ${request::class.simpleName} to handler ${handler::class.simpleName}")
            handler.handle(request)
        }, executor::execute)
    }

    override fun emit(@Valid event: Event) {
        val eventHandlers = registry.get(event::class.java)
        eventHandlers.forEach { handler ->
            logger.debug("Dispatching ${event::class.simpleName} to handler ${handler::class.simpleName}")
            handler.handle(event)
        }
    }

    override fun emitAsync(@Valid event: Event): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            val eventHandlers = registry.get(event::class.java)
            eventHandlers.forEach { handler ->
                logger.debug("Dispatching ${event::class.simpleName} to handler ${handler::class.simpleName}")
                handler.handle(event) }
        }, executor::execute)
    }

    override fun dispatch(@Valid command: Command) {
        val handler = registry.get(command::class.java)
        logger.debug("Dispatching ${command::class.simpleName} to handler ${handler::class.simpleName}")
        handler.handle(command)
    }

    override fun dispatchAsync(@Valid command: Command): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            val handler = registry.get(command::class.java)
            logger.debug("Dispatching ${command::class.simpleName} to handler ${handler::class.simpleName}")
            handler.handle(command)
        }, executor::execute)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(SpringMediator::class.java)
    }
}