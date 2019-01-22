package io.jkratz.mediatr.core.exception

/**
 *
 */
class NoCommandHandlerException(message: String?): RuntimeException(message)

/**
 *
 */
class MultipleCommandHandlerException(message: String?): RuntimeException(message)

/**
 *
 */
class CommandHandlerException: RuntimeException {

    constructor(message: String?): super(message)

    constructor(message: String?, throwable: Throwable): super(message, throwable)
}