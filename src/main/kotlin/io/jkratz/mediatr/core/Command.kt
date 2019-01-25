package io.jkratz.mediatr.core

/**
 * Marker interface for a Command
 *
 * @author Joseph Kratz
 * @since 1.0
 */
interface Command

/**
 * A handler for a given Command
 *
 * @author Joseph Kratz
 * @since 1.0
 */
interface CommandHandler<TCommand> where TCommand: Command {

    fun handle(command: Command)
}