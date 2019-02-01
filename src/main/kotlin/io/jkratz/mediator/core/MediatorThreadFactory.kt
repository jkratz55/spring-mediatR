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

package io.jkratz.mediator.core

import java.util.concurrent.Executor
import java.util.concurrent.ThreadFactory

/**
 * Custom implementation of [ThreadFactory] to provide custom naming
 * of the threads used by the default [Executor] for asynchronous processing.
 *
 * @author Joseph Kratz
 * @since 1.0
 */
class MediatorThreadFactory: ThreadFactory {

    private var counter: Int = 0

    /**
     * Creates threads with the naming scheme Mediator-X where X is the
     * thread number
     *
     * Example: Mediator-1
     */
    override fun newThread(r: Runnable?): Thread {
        counter++
        return Thread(r, "Mediator-$counter")
    }
}