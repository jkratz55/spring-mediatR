package io.github.jkratz55.mediator.mock

import io.github.jkratz55.mediator.core.Command
import io.github.jkratz55.mediator.core.CommandHandler

class SayHelloCommand(val message: String): Command

class SayHelloCommandHandler: CommandHandler<SayHelloCommand> {

    override fun handle(command: SayHelloCommand) {
        println(command.message)
    }
}