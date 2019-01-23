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

import io.jkratz.mediatr.core.Command
import io.jkratz.mediatr.core.CommandHandler
import io.jkratz.mediatr.core.Event
import io.jkratz.mediatr.core.EventHandler
import io.jkratz.mediatr.core.exception.MultipleCommandHandlerException
import io.jkratz.mediatr.core.exception.NoCommandHandlerException
import io.jkratz.mediatr.core.exception.NoEventHandlersException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import java.util.HashMap
import org.springframework.core.GenericTypeResolver

/**
 * @author Joseph Kratz
 * @since 1.0
 */
internal class Registry(val applicationContext: ApplicationContext) {

    private val commandRegistry: MutableMap<Class<out Command<*>>, CommandProvider<*>> = HashMap()
    private val eventRegistry: MutableMap<Class<out Event>, MutableSet<EventProvider<*>>> = HashMap()

    init {
        applicationContext.getBeanNamesForType(CommandHandler::class.java)
            .forEach { registerCommandHandler(it) }

        applicationContext.getBeanNamesForType(EventHandler::class.java)
            .forEach { registerEventHandler(it) }
    }

    fun <C : Command<R>,R> get(commandClass: Class<out C>): CommandHandler<C,R> {
        commandRegistry[commandClass]?.let { provider ->
            return provider.get() as CommandHandler<C, R>
        } ?: throw NoCommandHandlerException("No CommandHandler is registered to handle command of type ${commandClass.canonicalName}")
    }

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

    private fun registerCommandHandler(name: String) {
        logger.debug("Registering CommandHandler with name $name")
        val handler: CommandHandler<*,*> = applicationContext.getBean(name) as CommandHandler<*,*>
        val generics = GenericTypeResolver.resolveTypeArguments(handler::class.java, CommandHandler::class.java)
        generics?.let {
            val commandType =  it[0] as Class<out Command<*>>
            if (commandRegistry.contains(commandType)) {
                throw MultipleCommandHandlerException("${commandType.canonicalName} already has a registered handler. Each command must have one CommandHandler only")
            }
            val commandProvider = CommandProvider(applicationContext, handler::class)
            commandRegistry[commandType] = commandProvider
            logger.info("Registered handler ${handler::class.simpleName} to handle command ${commandType.simpleName}")
        }
    }

    private fun registerEventHandler(name: String) {
        logger.debug("Registering EventHandler with name $name")
        val eventHandler: EventHandler<*> = applicationContext.getBean(name) as EventHandler<*>
        val generics = GenericTypeResolver.resolveTypeArguments(eventHandler::class.java, EventHandler::class.java)
        generics?.let {
            val eventType = it[0] as Class<out Event>
            val eventProvider = EventProvider(applicationContext, eventHandler::class)
            eventRegistry[eventType]?.add(eventProvider) ?: kotlin.run {
                eventRegistry[eventType] = mutableSetOf(eventProvider)
            }
            logger.info("Register EventHandler ${eventHandler::class.simpleName} to handle Event ${eventType.simpleName}")
        }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(Registry::class.java)
    }
}