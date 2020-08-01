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
import org.springframework.core.GenericTypeResolver
import kotlin.collections.HashMap

/**
 * This class is responsible for registering beans from the Spring ApplicationContext
 * that implements the [RequestHandler] interface or [EventHandler] interface to the specific
 * [Event] or [Request] they are to handle. This class is also used to retrieve the handler
 * based on the type of the event or request.
 *
 * In order to address potential issues with circular dependencies the handlers are lazy registered.
 * The registration takes place once on the first lookup of a handler.
 *
 * @author Joseph Kratz
 * @since 1.0
 * @property applicationContext Context from the Spring container
 */
class SpringRegistry(private val applicationContext: ApplicationContext): Registry {

    private val requestRegistry: MutableMap<Class<out Request<*>>, RequestHandlerProvider<*>> = HashMap()
    private val eventRegistry: MutableMap<Class<out Event>, MutableSet<EventHandlerProvider<*>>> = HashMap()
    private val commandRegistry: MutableMap<Class<out Command>, CommandHandlerProvider<*>> = HashMap()
    private var initialized: Boolean = false

    override fun <C : Request<R>,R> get(requestClass: Class<out C>): RequestHandler<C,R> {
        if (!initialized) {
            initializeHandlers()
        }
        requestRegistry[requestClass]?.let { provider ->
            return provider.handler as RequestHandler<C, R>
        } ?: throw NoRequestHandlerException("No RequestHandler is registered to handle request of type ${requestClass.canonicalName}")
    }

    override fun <E: Event> get(eventClass: Class<out E>): Set<EventHandler<E>> {
        if (!initialized) {
            initializeHandlers()
        }
        val handlers = mutableSetOf<EventHandler<E>>()
        eventRegistry[eventClass]?.let { providers ->
            for (provider in providers) {
                val handler = provider.handler as EventHandler<E>
                handlers.add(handler)
            }
        } ?: throw NoEventHandlersException("No EventHandlers are registered to handle event of type ${eventClass.canonicalName}")
        return handlers
    }

    override fun <C: Command> get(commandClass: Class<out C>): CommandHandler<C> {
        if (!initialized) {
            initializeHandlers()
        }
        commandRegistry[commandClass]?.let { provider ->
            return provider.handler as CommandHandler<C>
        } ?: throw NoCommandHandlerException("No CommandHandler is registered to handle request of type ${commandClass.canonicalName}")
    }

    /**
     * Initializes all the handlers from the ApplicationContext
     */
    private fun initializeHandlers() {
        synchronized(this) {
            if (!initialized) {
                applicationContext.getBeanNamesForType(RequestHandler::class.java)
                    .forEach { registerRequestHandler(it) }
                applicationContext.getBeanNamesForType(EventHandler::class.java)
                    .forEach { registerEventHandler(it) }
                applicationContext.getBeanNamesForType(CommandHandler::class.java)
                    .forEach { registerCommandHandler(it) }
                initialized = true
            }
        }
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
        val logger: Logger = LoggerFactory.getLogger(SpringRegistry::class.java)
    }
}