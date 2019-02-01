package io.jkratz.mediator.spring

import io.jkratz.mediator.core.CommandHandler
import io.jkratz.mediator.core.EventHandler
import io.jkratz.mediator.core.RequestHandler
import io.jkratz.mediator.mock.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.context.ApplicationContext
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class SpringMediatorTest {

    @Mock
    private lateinit var applicationContext: ApplicationContext

    @Mock
    private lateinit var commandHandler: SayHelloCommandHandler

    @Mock
    private lateinit var requestHandler: CalculateOrderTotalRequestHandler

    @Mock
    private lateinit var eventHandler: OrderCreationListener

    private lateinit var mediator: SpringMediator

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

        mediator = SpringMediator(applicationContext)
    }

    @Test
    fun testCommandDispatch() {
        val command = SayHelloCommand("Hello Friends!")
        mediator.dispatch(command)
        Mockito.verify(commandHandler).handle(command)
    }

    @Test
    fun testCommandDispatchAsync() {
        val command = SayHelloCommand("Hello Friends!")
        mediator.dispatchAsync(command)
            .exceptionally { throw RuntimeException() }
            .thenAccept { Mockito.verify(commandHandler).handle(command) }
    }

    @Test
    fun testRequestDispatch() {
        val request = CalculateOrderTotalRequest(BigDecimal(15.99), BigDecimal.TEN)
        mediator.dispatch(request)
        Mockito.verify(requestHandler).handle(request)
    }

    @Test
    fun testRequestDispatchAsync() {
        val request = CalculateOrderTotalRequest(BigDecimal(15.99), BigDecimal.TEN)
        mediator.dispatchAsync(request)
            .exceptionally { throw RuntimeException() }
            .thenAccept { Mockito.verify(requestHandler).handle(request) }
    }

    @Test
    fun testEventEmit() {
        val event = OrderCreatedEvent(55)
        mediator.emit(event)
        Mockito.verify(eventHandler).handle(event)
    }

    @Test
    fun testEventEmitAysnc() {
        val event = OrderCreatedEvent(55)
        mediator.emitAsync(event)
            .thenAccept { Mockito.verify(eventHandler).handle(event) }
    }
}