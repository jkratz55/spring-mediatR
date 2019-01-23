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

package io.jkratz.mediatr.core.decorator

import io.jkratz.mediatr.core.Command
import io.jkratz.mediatr.core.Event
import io.jkratz.mediatr.core.MediatR
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.system.measureTimeMillis

class LoggingMediatR(private val decorated: MediatR): MediatR {

    override fun <TCommand : Command<TResponse>, TResponse> execute(command: TCommand): TResponse {
//        var result: TResponse
//        val time = measureTimeMillis {
//            result = decorated.execute(command)
//        }
//        logger.info("Executed ${command::class.simpleName} in $time ms")
//        return result

        return decorated.execute(command)
    }

    override fun <TCommand : Command<TResponse>, TResponse> executeAsync(
        command: TCommand,
        executor: Executor?
    ): CompletableFuture<TResponse> {
        return decorated.executeAsync(command, executor)
    }

    override fun emit(event: Event) {
        return decorated.emit(event)
    }

    override fun emitAsync(event: Event, executor: Executor?): CompletableFuture<Void> {
        return decorated.emitAsync(event, executor)
    }

    companion object {
        val logger = LoggerFactory.getLogger(LoggingMediatR::class.java)
    }
}