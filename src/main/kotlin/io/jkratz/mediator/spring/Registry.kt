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
import io.jkratz.mediator.core.exception.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import java.util.HashMap
import org.springframework.core.GenericTypeResolver

/**
 * This class is responsible for registering beans from the Spring ApplicationContext
 * that implements the [RequestHandler] interface or [EventHandler] interface to the specific
 * [Event] or [Request] they are to handle. This class is also used to retrieve the handler
 * based on the type of the event or request.
 *
 * @author Joseph Kratz
 * @since 1.0
 * @property applicationContext Context from the Spring container
 */
internal class Registry(private val applicationContext: ApplicationContext) {

    private val requestRegistry: MutableMap<Class<out Request<*>>, RequestHandlerProvider<*>> = HashMap()
    private val eventRegistry: MutableMap<Class<out Event>, MutableSet<EventHandlerProvider<*>>> = HashMap()
    private val commandRegistry: MutableMap<Class<out Command>, CommandHandlerProvider<*>> = HashMap()

    init {
        applicationContext.getBeanNamesForType(RequestHandler::class.java)
            .forEach { registerRequestHandler(it) }

        applicationContext.getBeanNamesForType(EventHandler::class.java)
            .forEach { registerEventHandler(it) }

        applicationContext.getBeanNamesForType(CommandHandler::class.java)
            .forEach { registerCommandHandler(it) }
    }

    /**
     * Retrieves the RequestHandler for the provided type. If not RequestHandler is
     * registered to handle the type provided [NoRequestHandlerException] will be thrown
     *
     * @param requestClass The type of the request
     * @return The RequestHandler for the request
     * @throws NoRequestHandlerException When there is not a RequestHandler available for the request
     */
    fun <C : Request<R>,R> get(requestClass: Class<out C>): RequestHandler<C,R> {
        requestRegistry[requestClass]?.let { provider ->
            return provider.get() as RequestHandler<C, R>
        } ?: throw NoRequestHandlerException("No RequestHandler is registered to handle request of type ${requestClass.canonicalName}")
    }

    /**
     * Retrieves all the EventHandlers for the provided event type. If no EventHandlers are
     * registered to handle the type provided [NoEventHandlersException] will be thrown.
     *
     * @param eventClass The type of the event
     * @return Set of EventHandlers for the eventClass
     * @throws NoEventHandlersException When there are no EventHandlers available
     */
    fun <E: Event> get(eventClass: Class<out E>): Set<EventHandler<E>> {
        val handlers = mutableSetOf<EventHandler<E>>()
        eventRegistry[eventClass]?.let { providers ->
            for (provider in providers) {
                val handler = provider.get() as EventHandler<E>
                handlers.add(handler)
            }
        } ?: throw NoEventHandlersException("No EventHandlers are registered to handle event of type ${eventClass.canonicalName}")
        return handlers
    }

    /**
     * Retrieves a CommandHandler for the provided type. If no CommandHandler
     * is registered for the Command type [NoCommandHandlerException] will be thrown.
     *
     * @param commandClass The type of the command
     * @return The CommandHandler for the command
     * @throws NoCommandHandlerException When there isn't a CommandHandler available
     */
    fun <C: Command> get(commandClass: Class<out C>): CommandHandler<C> {
        commandRegistry[commandClass]?.let { provider ->
            return provider.get() as CommandHandler<C>
        } ?: throw NoCommandHandlerException("No CommandHandler is registered to handle request of type ${commandClass.canonicalName}")
    }

    /**
     * Registers a RequestHandler from the Spring context by name
     *
     * @param name Name of the bean to register as a CommandHandler
     */
    private fun registerRequestHandler(name: String) {
        logger.debug("Registering RequestHandler with name $name")
        val handler: RequestHandler<*,*> = applicationContext.getBean(name) as RequestHandler<*,*>
        val generics = GenericTypeResolver.resolveTypeArguments(handler::class.java, RequestHandler::class.java)
        generics?.let {
            val requestType =  it[0] as Class<out Request<*>>
            if (requestRegistry.contains(requestType)) {
                throw DuplicateRequestHandlerRegistrationException("${requestType.canonicalName} already has a registered handler. Each request must have a single request handler")
            }
            val requestProvider = RequestHandlerProvider(applicationContext, handler::class)
            requestRegistry[requestType] = requestProvider
            logger.info("Registered RequestHandler ${handler::class.simpleName} to handle Request ${requestType.simpleName}")
        }
    }

    /**
     * Registers an EventHandler from the Spring context by name
     *
     * @param name Name of the bean to register as an EventHandler
     */
    private fun registerEventHandler(name: String) {
        logger.debug("Registering EventHandler with name $name")
        val eventHandler: EventHandler<*> = applicationContext.getBean(name) as EventHandler<*>
        val generics = GenericTypeResolver.resolveTypeArguments(eventHandler::class.java, EventHandler::class.java)
        generics?.let {
            val eventType = it[0] as Class<out Event>
            val eventProvider = EventHandlerProvider(applicationContext, eventHandler::class)
            eventRegistry[eventType]?.add(eventProvider) ?: kotlin.run {
                eventRegistry[eventType] = mutableSetOf(eventProvider)
            }
            logger.info("Register EventHandler ${eventHandler::class.simpleName} to handle Event ${eventType.simpleName}")
        }
    }

    /**
     * Registers a CommandHandler from the Spring context by name
     *
     * @param name Name of the bean to register as a CommandHandler
     */
    private fun registerCommandHandler(name: String) {
        logger.debug("Registering CommandHandler with name $name")
        val commandHandler: CommandHandler<*> = applicationContext.getBean(name) as CommandHandler<*>
        val generics = GenericTypeResolver.resolveTypeArguments(commandHandler::class.java, CommandHandler::class.java)
        generics?.let {
            val commandType = it[0] as Class<out Command>
            if (commandRegistry.containsKey(commandType)) {
                throw DuplicateCommandHandlerRegistrationException("${commandType.canonicalName} already has a registered handler. Each command must have a single command handler")
            }
            val commandHandlerProvider = CommandHandlerProvider(applicationContext, commandHandler::class)
            commandRegistry[commandType] = commandHandlerProvider
            logger.info("Registered CommandHandler ${commandHandler::class.simpleName} to handle Command ${commandType.simpleName}")
        }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(Registry::class.java)
    }
}