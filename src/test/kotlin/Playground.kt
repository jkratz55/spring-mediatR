import io.jkratz.mediatr.core.Command
import io.jkratz.mediatr.core.CommandHandler
import io.jkratz.mediatr.spring.CommandProvider
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.core.GenericTypeResolver
import kotlin.reflect.KClass

class ChangeNameCommand: Command<String> {

    val newName = "Bob"
}

class ChangeNameCommandHandler: CommandHandler<ChangeNameCommand, String> {

    override fun handle(command: ChangeNameCommand): String {
        println("Name changed to ${command.newName}")
        return "Bob"
    }
}

class Tester {

    @Test
    fun play() {

        //val registry: MutableMap<Command<Any>, CommandHandler<Command<Any>, Any>> = HashMap()
        //val classRegistry: MutableMap<KClass<Command<Any>>, KClass<CommandHandler<Command<Any>, Any>>> = HashMap()
        //val classRegistry: MutableMap<KClass<*>, KClass<*>> = HashMap()
        val commandRegistry: MutableMap<KClass<*>, CommandProvider<*>> = HashMap()

        val command = ChangeNameCommand()
        val handler = ChangeNameCommandHandler()

        var applicationContext: ApplicationContext? = null
        //val generics = GenericTypeResolver.resolveTypeArguments(handler::class.java, CommandHandler::class.java)
        //val commandType = generics[0]
        //println(generics)
        //println(commandType)

        //val superTypes = handler::class.supertypes
        //val typeTypes = handler::class.typeParameters

        //val commandKClass = command::class
       // val commandHandlerKClass = handler::class

        val provider = CommandProvider(applicationContext!!, handler::class)
        commandRegistry[command::class] = provider

        //classRegistry.put(commandKClass, commandHandlerKClass::class)


        //val x = superTypes[0]
        //val y = superTypes[1]
    }

    @Test
    fun testFun() {

        val command: Class<out Command<*>> = ChangeNameCommand()::class.java
        val handler: CommandHandler<*,*> = ChangeNameCommandHandler()
        val generics = GenericTypeResolver.resolveTypeArguments(handler::class.java, CommandHandler::class.java)
        val commandType =  generics[0]
        println(commandType)
    }
}