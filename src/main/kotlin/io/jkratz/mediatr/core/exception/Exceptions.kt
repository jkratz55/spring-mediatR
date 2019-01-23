/*
 * Copyright 2018 the original author or authors.
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

/**
 *
 */
class NoEventHandlersException(message: String?): RuntimeException(message)