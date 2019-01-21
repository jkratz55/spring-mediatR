package io.jkratz.mediatr.spring

import io.jkratz.mediatr.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.GenericTypeResolver
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.HashMap
import io.jkratz.mediatr.core.Command
import kotlin.reflect.KClass


class SpringMediatR @Autowired constructor(val applicationContext: ApplicationContext): MediatR {


    init {

        // Get all beans that implement CommandHandler interface and register them
        applicationContext.getBeanNamesForType(CommandHandler::class.java)
            .forEach { registerCommands(it) }
    }

    private fun registerCommands(name: String) {
        applicationContext.getType(name)?.let {handler ->
            val generics = GenericTypeResolver.resolveTypeArguments(handler, CommandHandler::class.java)
            val commandType = generics[1]
            //commandRegistry.put(commandType, handler)
        }

    }

    override fun <TResponse> dispatch(command: Command<TResponse>): TResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <TResponse> dispatchAsync(command: Command<TResponse>): CompletableFuture<TResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <TResponse> dispatchAsync(
        command: Command<TResponse>,
        executor: Executor
    ): CompletableFuture<TResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emit(event: Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emitAsync(event: Event): CompletableFuture<Void> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emitAsync(event: Event, executor: Executor): CompletableFuture<Void> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}