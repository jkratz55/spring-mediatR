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