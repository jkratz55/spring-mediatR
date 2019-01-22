package io.jkratz.mediatr.core.decorator

import io.jkratz.mediatr.core.Command
import io.jkratz.mediatr.core.Event
import io.jkratz.mediatr.core.MediatR
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

//class LoggingMediatR: MediatR {
//
//    override fun <TResponse> execute(command: Command<TResponse>): TResponse {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun <TResponse> executeAsync(command: Command<TResponse>): CompletableFuture<TResponse> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun <TResponse> executeAsync(
//        command: Command<TResponse>,
//        executor: Executor
//    ): CompletableFuture<TResponse> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun emit(event: Event) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun emitAsync(event: Event): CompletableFuture<Void> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun emitAsync(event: Event, executor: Executor): CompletableFuture<Void> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}