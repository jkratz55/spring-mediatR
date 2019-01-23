package io.jkratz.mediatr.spring

import io.jkratz.mediatr.core.Command
import io.jkratz.mediatr.core.CommandHandler
import io.jkratz.mediatr.core.Event
import io.jkratz.mediatr.core.EventHandler
import io.jkratz.mediatr.core.exception.MultipleCommandHandlerException
import io.jkratz.mediatr.core.exception.NoCommandHandlerException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import java.util.HashMap
import org.springframework.core.GenericTypeResolver

/**
 *
 */
internal class Registry(val applicationContext: ApplicationContext) {

    private val commandRegistry: MutableMap<Class<out Command<*>>, CommandProvider<*>> = HashMap()
    private val eventRegistry: MutableMap<Class<out Event>, EventProvider<*>> = HashMap()

    init {
        applicationContext.getBeanNamesForType(CommandHandler::class.java)
            .forEach { registerCommandHandler(it) }

        applicationContext.getBeanNamesForType(EventHandler::class.java)
            .forEach { registerEventHandler(it) }
    }

    fun <C : Command<R>,R> get(commandClass: Class<out C>): CommandHandler<C,R> {
        commandRegistry[commandClass]?.let {provider ->
            return provider.get() as CommandHandler<C, R>
        } ?: throw throw NoCommandHandlerException("No CommandHandler is registered to handle command of type ${commandClass.canonicalName}")
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
        logger.debug("Register EventHandler with name $name")
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(Registry::class.java)
    }
}