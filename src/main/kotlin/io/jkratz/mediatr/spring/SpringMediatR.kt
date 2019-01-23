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

package io.jkratz.mediatr.spring

import io.jkratz.mediatr.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import io.jkratz.mediatr.core.Command
import java.util.function.Supplier
import javax.validation.Valid

/**
 *
 */
class SpringMediatR @Autowired constructor(applicationContext: ApplicationContext): MediatR {

    private val registry: Registry = Registry(applicationContext)

    override fun <TCommand: Command<TResponse>, TResponse> execute(@Valid command: TCommand): TResponse {
        val commandHandler = registry.get(command::class.java)
        return commandHandler.handle(command)
    }

    override fun <TCommand : Command<TResponse>, TResponse> executeAsync(@Valid command: TCommand, executor: Executor?): CompletableFuture<TResponse> {
        val commandHandler = registry.get(command::class.java)
        executor?.let { exec ->
            return CompletableFuture.supplyAsync(Supplier {
                commandHandler.handle(command)
            }, exec)
        } ?:
        return CompletableFuture.supplyAsync {
            commandHandler.handle(command)
        }
    }

    override fun emit(@Valid event: Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emitAsync(@Valid event: Event, executor: Executor?): CompletableFuture<Void> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}