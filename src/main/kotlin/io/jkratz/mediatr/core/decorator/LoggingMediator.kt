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

package io.jkratz.mediatr.core.decorator

import io.jkratz.mediatr.core.Request
import io.jkratz.mediatr.core.Event
import io.jkratz.mediatr.core.Mediator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.system.measureTimeMillis

//class LoggingMediator(private val decorated: Mediator): Mediator {
//
//    override fun <TRequest : Request<TResponse>, TResponse> dispatch(request: TRequest): TResponse {
//        var result: TResponse? = null
//        val time = measureTimeMillis {
//            result = decorated.dispatch(request)
//        }
//        logger.info("Executed ${request::class.simpleName} in $time ms")
//        return result!!
//    }
//
//    override fun <TRequest : Request<TResponse>, TResponse> dispatchAsync(request: TRequest): CompletableFuture<TResponse> {
//        var result:CompletableFuture<TResponse> = CompletableFuture()
//        val time = measureTimeMillis {
//            result = decorated.dispatchAsync(request)
//        }
//        logger.info("Executed ${request::class.simpleName} in $time ms")
//        return result
//    }
//
//    override fun <TRequest : Request<TResponse>, TResponse> dispatchAsync(
//        request: TRequest,
//        executor: Executor
//    ): CompletableFuture<TResponse> {
//        var result:CompletableFuture<TResponse> = CompletableFuture()
//        val time = measureTimeMillis {
//            result = decorated.dispatchAsync(request, executor)
//        }
//        logger.info("Executed ${request::class.simpleName} in $time ms")
//        return result
//    }
//
//    override fun emit(event: Event) {
//        val time = measureTimeMillis {
//            decorated.emit(event)
//        }
//        logger.info("Executed ${event::class.simpleName} in $time ms")
//    }
//
//    override fun emitAsync(event: Event): CompletableFuture<Void> {
//        var result: CompletableFuture<Void> = CompletableFuture()
//        val time = measureTimeMillis {
//            result = decorated.emitAsync(event)
//        }
//        logger.info("Executed ${event::class.simpleName} in $time ms")
//        return result
//    }
//
//    override fun emitAsync(event: Event, executor: Executor): CompletableFuture<Void> {
//        var result: CompletableFuture<Void> = CompletableFuture()
//        val time = measureTimeMillis {
//            result = decorated.emitAsync(event,executor)
//        }
//        logger.info("Executed ${event::class.simpleName} in $time ms")
//        return result
//    }
//
//    companion object {
//        val logger: Logger = LoggerFactory.getLogger(LoggingMediator::class.java)
//    }
//}