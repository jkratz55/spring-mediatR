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

package io.jkratz.mediator.core

import io.jkratz.mediator.core.exception.NoCommandHandlerException
import io.jkratz.mediator.core.exception.NoEventHandlersException
import io.jkratz.mediator.core.exception.NoRequestHandlerException

/**
 * A registry for handlers for messages that can be dispatched in Spring MediatR
 *
 * @author Joseph Kratz
 * @since 1.1
 */
interface Registry {

    /**
     * Retrieves the RequestHandler for the provided type. If not RequestHandler is
     * registered to handle the type provided [NoRequestHandlerException] will be thrown
     *
     * @param requestClass The type of the request
     * @return The RequestHandler for the request
     * @throws NoRequestHandlerException When there is not a RequestHandler available for the request
     */
    fun <C : Request<R>,R> get(requestClass: Class<out C>): RequestHandler<C,R>

    /**
     * Retrieves all the EventHandlers for the provided event type. If no EventHandlers are
     * registered to handle the type provided [NoEventHandlersException] will be thrown.
     *
     * @param eventClass The type of the event
     * @return Set of EventHandlers for the eventClass
     * @throws NoEventHandlersException When there are no EventHandlers available
     */
    fun <E: Event> get(eventClass: Class<out E>): Set<EventHandler<E>>

    /**
     * Retrieves a CommandHandler for the provided type. If no CommandHandler
     * is registered for the Command type [NoCommandHandlerException] will be thrown.
     *
     * @param commandClass The type of the command
     * @return The CommandHandler for the command
     * @throws NoCommandHandlerException When there isn't a CommandHandler available
     */
    fun <C: Command> get(commandClass: Class<out C>): CommandHandler<C>
}