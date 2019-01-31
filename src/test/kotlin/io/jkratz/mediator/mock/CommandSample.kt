package io.jkratz.mediator.mock

import io.jkratz.mediator.core.Command
import io.jkratz.mediator.core.CommandHandler

class SayHelloCommand(val message: String): Command

class SayHelloCommandHandler: CommandHandler<SayHelloCommand> {

    override fun handle(command: SayHelloCommand) {
        println(command.message)
    }
}