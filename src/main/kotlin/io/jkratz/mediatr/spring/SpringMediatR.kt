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


class SpringMediatR @Autowired constructor(private val applicationContext: ApplicationContext): MediatR {

    private val registry: Registry = Registry(applicationContext)

    override fun <TCommand: Command<TResponse>, TResponse> execute(command: TCommand): TResponse {
        val commandHandler = registry.get<TCommand, TResponse>(command::class.java)
        return commandHandler.handle(command)
    }

    override fun <TResponse> executeAsync(command: Command<TResponse>): CompletableFuture<TResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <TResponse> executeAsync(
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