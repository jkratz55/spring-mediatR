package io.jkratz.mediatr.core.decorator

import io.jkratz.mediatr.core.Command
import io.jkratz.mediatr.core.Event
import io.jkratz.mediatr.core.MediatR
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.system.measureTimeMillis

class LoggingMediatR(private val decorated: MediatR): MediatR {

    override fun <TCommand : Command<TResponse>, TResponse> execute(command: TCommand): TResponse {
//        var result: TResponse
//        val time = measureTimeMillis {
//            result = decorated.execute(command)
//        }
//        logger.info("Executed ${command::class.simpleName} in $time ms")
//        return result

        return decorated.execute(command)
    }

    override fun <TCommand : Command<TResponse>, TResponse> executeAsync(
        command: TCommand,
        executor: Executor?
    ): CompletableFuture<TResponse> {
        return decorated.executeAsync(command, executor)
    }

    override fun emit(event: Event) {
        return decorated.emit(event)
    }

    override fun emitAsync(event: Event, executor: Executor?): CompletableFuture<Void> {
        return decorated.emitAsync(event, executor)
    }

    companion object {
        val logger = LoggerFactory.getLogger(LoggingMediatR::class.java)
    }
}