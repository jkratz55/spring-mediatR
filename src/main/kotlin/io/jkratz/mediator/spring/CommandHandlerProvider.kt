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

package io.jkratz.mediator.spring

import io.jkratz.mediator.core.CommandHandler
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

/**
 * A wrapper around a CommandHandler
 *
 * @author Joseph Kratz
 * @since 1.0
 * @property applicationContext ApplicationContext from Spring used to retrieve beans
 * @property type Type of CommandHandler
 */
internal class CommandHandlerProvider<T> (
    private val applicationContext: ApplicationContext,
    private val type: KClass<T>
) where T: CommandHandler<*> {

    internal val handler: T by lazy {
        applicationContext.getBean(type.java)
    }
}