package io.jkratz.spring.mediator

interface CommandHandler<TCommand> {

    fun handle(command: TCommand)
}