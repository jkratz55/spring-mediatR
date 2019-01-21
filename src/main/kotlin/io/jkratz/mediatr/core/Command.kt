package io.jkratz.mediatr.core

/**
 * Marker interface for a command
 *
 * @author Joseph Kratz
 * @since 1.0
 * @param <TResponse> type of the return value
 */
interface Command<out TResponse>

/**
 * A handler for a command
 *
 * @author Joseph Kratz
 * @since 1.0
 * @param <TCommand> the type of command to be handled
 * @param <TResponse> the type of the response
 */
interface CommandHandler<in TCommand, TResponse> where TCommand: Command<TResponse> {

    /**
     * Handles the command
     *
     * @param command command to handle
     * @return the response of the command
     */
    fun handle(command: TCommand): TResponse
}