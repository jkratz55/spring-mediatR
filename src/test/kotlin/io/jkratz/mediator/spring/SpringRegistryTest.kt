package io.jkratz.mediator.spring

import io.jkratz.mediator.core.CommandHandler
import io.jkratz.mediator.core.EventHandler
import io.jkratz.mediator.core.Registry
import io.jkratz.mediator.core.RequestHandler
import io.jkratz.mediator.mock.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.context.ApplicationContext
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class SpringRegistryTest {

    @Mock
    private lateinit var applicationContext: ApplicationContext

    @Mock
    private lateinit var commandHandler: SayHelloCommandHandler

    @Mock
    private lateinit var requestHandler: CalculateOrderTotalRequestHandler

    @Mock
    private lateinit var eventHandler: OrderCreationListener

    private lateinit var registry: Registry

    @Before
    fun setup() {

        Mockito.`when`(applicationContext.getBeanNamesForType(CommandHandler::class.java))
            .thenReturn(arrayOf("sayHelloCommandHandler"))

        Mockito.`when`(applicationContext.getBeanNamesForType(RequestHandler::class.java))
            .thenReturn(arrayOf("calculateOrderTotalRequestHandler"))

        Mockito.`when`(applicationContext.getBeanNamesForType(EventHandler::class.java))
            .thenReturn(arrayOf("orderCreationListener"))

        Mockito.`when`(applicationContext.getBean("calculateOrderTotalRequestHandler"))
            .thenReturn(requestHandler)

        Mockito.`when`(applicationContext.getBean("orderCreationListener"))
            .thenReturn(eventHandler)

        Mockito.`when`(applicationContext.getBean("sayHelloCommandHandler"))
            .thenReturn(commandHandler)

        Mockito.`when`(applicationContext.getBean(SayHelloCommandHandler::class.java))
            .thenReturn(commandHandler)

        Mockito.`when`(applicationContext.getBean(CalculateOrderTotalRequestHandler::class.java))
            .thenReturn(requestHandler)

        Mockito.`when`(applicationContext.getBean(OrderCreationListener::class.java))
            .thenReturn(eventHandler)

        this.registry = SpringRegistry(applicationContext)
    }

    @Test
    fun testGetCommandHandler() {
        val handlers = this.registry.get(OrderCreatedEvent::class.java)
        assertTrue(handlers.isNotEmpty())
        assertTrue(handlers.contains(this.eventHandler))
    }

    @Test
    fun testGetEventHandler() {
        val handler = this.registry.get(SayHelloCommand::class.java)
        assertEquals(this.commandHandler, handler)
    }

    @Test
    fun testGetRequestHandler() {
        val handler = this.registry.get(CalculateOrderTotalRequest::class.java)
        assertEquals(this.requestHandler, handler)
    }
}