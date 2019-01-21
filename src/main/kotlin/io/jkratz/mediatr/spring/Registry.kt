package io.jkratz.mediatr.spring

import io.jkratz.mediatr.core.Command
import io.jkratz.mediatr.core.CommandHandler
import org.springframework.context.ApplicationContext
import java.util.HashMap
import org.springframework.core.GenericTypeResolver

internal class Registry(val applicationContext: ApplicationContext) {

    val commandRegistry: MutableMap<Class<out Command<*>>, CommandProvider<*>> = HashMap()

    init {
        applicationContext.getBeanNamesForType(CommandHandler::class.java)
            .forEach { register(it) }
    }

    @SuppressWarnings("unchecked")
    private fun register(name: String) {
//        val handlerClass = applicationContext.getType(name) as Class<CommandHandler<*, *>>
//        val generics = GenericTypeResolver.resolveTypeArguments(handlerClass, CommandHandler::class.java)
//        val commandType = generics!![1] as Class<out Command<*>>
//        providerMap.put(commandType, CommandProvider(applicationContext, handlerClass))

        //val handlerClass = applicationContext.getType(name) as Class<CommandHandler<*,*>>
        val handler: CommandHandler<*,*> = applicationContext.getBean(name) as CommandHandler<*,*>
        val generics = GenericTypeResolver.resolveTypeArguments(handler::class.java, CommandHandler::class.java)
        generics?.let {
            val commandType =  generics[0] as Class<out Command<*>>
            val commandProvider = CommandProvider(applicationContext, handler::class)
            commandRegistry[commandType] = commandProvider
        }

        //val generics = GenericTypeResolver.resolveTypeArguments(handlerClass, CommandHandler::class.java)
        //val commandType = generics[0] as Class<out Command<*>>
        //val provider = CommandProvider(applicationContext, handlerClass::class)
    }
}