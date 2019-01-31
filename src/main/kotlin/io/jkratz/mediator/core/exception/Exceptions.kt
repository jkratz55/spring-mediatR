/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jkratz.mediator.core.exception

/**
 * Exception thrown when there is not a [RequestHandler] available for a [Request]
 *
 * @author Joseph Kratz
 * @since 1.0
 */
class NoRequestHandlerException(message: String?): RuntimeException(message)

/**
 * Exception thrown when there is an attempt to register a [RequestHandler] for a
 * [Request] that already has a [RequestHandler] registered.
 *
 * @author Joseph Kratz
 * @since 1.0
 */
class DuplicateRequestHandlerRegistrationException(message: String?): RuntimeException(message)

/**
 * Exception thrown when there are no [EventHandler]s available for an [Event]
 *
 * @author Joseph Kratz
 * @since 1.0
 */
class NoEventHandlersException(message: String?): RuntimeException(message)

/**
 * Exception thrown when there is not a [CommandHandler] available for a [Command]
 *
 * @author Joseph Kratz
 * @since 1.0
 */
class NoCommandHandlerException(message: String?): RuntimeException(message)

/**
 * Exception thrown when there is an attempt to register a [CommandHandler] for a
 * [Command] that already has a [CommandHandler] registered.
 *
 * @author Joseph Kratz
 * @since 1.0
 */
class DuplicateCommandHandlerRegistrationException(message: String?): RuntimeException(message)