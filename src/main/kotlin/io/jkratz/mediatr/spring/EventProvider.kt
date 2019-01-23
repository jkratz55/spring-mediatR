package io.jkratz.mediatr.spring

import io.jkratz.mediatr.core.EventHandler
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

/**
 *
 */
internal class EventProvider<T>(
    private val applicationContext: ApplicationContext,
    private val type: KClass<T>
) where T: EventHandler<*> {

    fun get(): T {
        return applicationContext.getBean(type.java)
    }
}