package io.jkratz.spring.mediator.sample

import io.jkratz.spring.mediator.Command
import io.jkratz.spring.mediator.CommandHandler

class CreateUserCommand: Command {

}

class CreateUserCommandHandler: CommandHandler<CreateUserCommand> {

    override fun handle(command: CreateUserCommand) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}