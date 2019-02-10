package io.jkratz.mediator.spring

import io.jkratz.mediator.core.CommandHandler
import io.jkratz.mediator.core.EventHandler
import io.jkratz.mediator.core.Registry
import io.jkratz.mediator.core.RequestHandler
import io.jkratz.mediator.mock.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.context.ApplicationContext
import java.math.BigDecimal

//@RunWith(MockitoJUnitRunner::class)
//class SpringMediatorTest {
//
//    @Mock
//    private lateinit var registry: Registry
//
//    @Mock
//    private lateinit var commandHandler: SayHelloCommandHandler
//
//    @Mock
//    private lateinit var requestHandler: CalculateOrderTotalRequestHandler
//
//    @Mock
//    private lateinit var eventHandler: OrderCreationListener
//
//    private lateinit var mediator: SpringMediator
//
//    @Before
//    fun setup() {
//
//        Mockito.`when`(registry.get(SayHelloCommand::class.java))
//            .thenReturn(commandHandler)
//
//        Mockito.`when`(registry.get(OrderCreatedEvent::class.java))
//            .thenReturn(setOf(eventHandler))
//
//        Mockito.`when`(registry.get(CalculateOrderTotalRequest::class.java))
//            .thenReturn(requestHandler)
//
//        mediator = spy(SpringMediator(registry))
//    }
//
//    @Test
//    fun testCommandDispatch() {
//        val command = SayHelloCommand("Hello Friends!")
//        mediator.dispatch(command)
//        Mockito.verify(commandHandler).handle(command)
//    }
//
//    @Test
//    fun testCommandDispatchAsync() {
//        val command = SayHelloCommand("Hello Friends!")
//        mediator.dispatchAsync(command)
//            .exceptionally { throw RuntimeException() }
//            .thenAccept { Mockito.verify(commandHandler).handle(command) }
//    }
//
//    @Test
//    fun testRequestDispatch() {
//        val request = CalculateOrderTotalRequest(BigDecimal(15.99), BigDecimal.TEN)
//        mediator.dispatch(request)
//        Mockito.verify(requestHandler).handle(request)
//    }
//
//    @Test
//    fun testRequestDispatchAsync() {
//        val request = CalculateOrderTotalRequest(BigDecimal(15.99), BigDecimal.TEN)
//        mediator.dispatchAsync(request)
//            .exceptionally { throw RuntimeException() }
//            .thenAccept { Mockito.verify(requestHandler).handle(request) }
//    }
//
//    @Test
//    fun testEventEmit() {
//        val event = OrderCreatedEvent(55)
//        mediator.emit(event)
//        Mockito.verify(eventHandler).handle(event)
//    }
//
//    @Test
//    fun testEventEmitAysnc() {
//        val event = OrderCreatedEvent(55)
//        mediator.emitAsync(event)
//            .thenAccept { Mockito.verify(eventHandler).handle(event) }
//    }
//}