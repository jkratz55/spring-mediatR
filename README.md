# Spring MediatR

An implementation of [MediatR](https://github.com/jbogard/MediatR) on the JVM for the Spring Framework. This project is heavily inspired by the [MediatR](https://github.com/jbogard/MediatR) project for .NET by Jimmy Bogard. Many of the concepts from [MediatR](https://github.com/jbogard/MediatR) have been adapted in this project for the JVM and Spring Framework.

Spring MediatR is a simple library intended to help developers write cleaner more focused code and decouple components. This library can also be used to implement some of the ideas and concepts from CQRS.

## Requirements

* Java 8 +
* Spring Framework 5 / Spring Boot 2*

*While Spring MediatR may work on older versions of Spring I have not verified or used it on older versions of Spring.*

## Getting Started

At this time the package is only available in jCenter but will eventually be published to Maven Central as well.

### Gradle

Add jcenter to the repositories


```groovy
repositories {
    jcenter()
    mavenCentral()
}
```

Add Spring MediatR dependency

```groovy
dependencies {
    
    // Other dependencies go here
    
    implementation 'io.jkratz.springmediatr:spring-mediatr:1.0-RELEASE'
}

```

Declare the Mediator bean

```java
@SpringBootApplication
public class SpringMediatrJavaSampleApplication {

	private final ApplicationContext applicationContext;

	@Autowired
	public SpringMediatrJavaSampleApplication(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public Mediator mediator() {
		return new SpringMediator(applicationContext);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringMediatrJavaSampleApplication.class, args);
	}
}

```

### Maven

Coming soon

### Samples

There is a sample project available on GitHub you can use to reference.

[Java Sample](https://github.com/jkratz55/spring-mediatr-java-sample)

[Kotlin Sample](https://github.com/jkratz55/spring-mediatr-kotlin-sample)


## Basics

Spring MediatR has three kinds of messages it dispatches:

* Request - Dispatches to a single handler, has return value
* Command - Dispatches to a single handler, does not provide a mechanism to return value
* Event - Dispatches to one or many handlers, does not provide a mechanism to return value

MediatR supports both synchronous and asynchronous dispatching. Asynchronous dispatching methods return a CompletableFuture. The asynchronous dispatching is ideal for long running operations where you don't want to hold the container thread hostage until the operation is completed. If the operation is fast it is more ideal to use the synchronous variants unless you have a particular reason not to.

#### A Word About Spring's @Transactional and Async Dispathcing/Emitting

Spring does a fantasic job of making dealing with transaction incredibly easy. But out of the box @Tranasctional does not work across multiple threads. If we start a transaction from one thread and try to commit or rollback the transaction from another thread, a runtime error will be generated complaining that the Spring transaction is not active on the current thread. Though we start and end the transaction from the same thread, we cannot perform database operations belong to transaction from another thread either.

There are ways to work around this limitation, but the following is not exhaustive. 

1. Manage the transaction manually. This can be done by injecting the TransationManager
2. Mark the handle method in the [X]Hander class as @Transactional and ensure all transactional work is done within that method on the same thread. 

### Request and RequestHandler

Requests and RequestHandlers can be used for both queries and commands. There are also Commands and CommandHandlers because of the limitations of the type system on the JVM due to type erasure. Generally if you need to return a value, use a Request, otherwise use a Command.

As an example a consumer might call your rest API requesting a user be created. If the created user needs to be immediatly available to the consumer you may need to return the ID of the user that was just created. This can be common when the API is expected to return a HTTP 201 status with the new resource link in the location header. In that case you would need to use a Request.

```java
public class CreateUserRequest implements Request<Integer> {
	
	private final String email;
	private final String password;
	
	public CreateUserRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	// getter methods omitted
}
```

```java
public class CreateUserRequestHandler implements RequestHandler<CreateUserRequest, Integer> {

	public Integer handle(CreateUserRequest request) {
		// do logic to create user and then return the ID
		return 42;
	}
}
```

On the other hand if you don't need to return a value you can use a command. See below.

### Command and CommandHandler

Commands and CommandHandlers are very similar to Request and RequestHandlers. The difference being that Commands do not
return a value. This makes the API a little more convenient for cases where you don't want a return value. With the Java/Kotlin
type system and type erasue it is not possible to have the same interface with and without generics. Commands and CommandHandlers also resonate closer to CQRS and Event Sourcing concepts. 

### Event and EventHandler

Events work simular to Commands except an Event can be handled by multiple event handlers. Events provide a mechanism to notify components in the application that something has happen. As an example a command may come into the system to create a User which upon successfully creating the user it emits a UserCreatedEvent, and any component interested in that event can be notified.

## Using MediatR

### Building Messages and Handlers

Messages and Handlers are the core of Spring MediatR. Messages carry the intent and data, while handlers as you might have guessed, handle the messages. Handlers can be thought of as a replacement for service classes, and because they are managed by Spring you can inject your dependencies like any other Spring managed class.

*Important: Always remember to annotation your handler classes with @Component. If the class is not managed by Spring it will fail to be registered and likely result in an exception at runtime.*

The following is an example of a command message and command handler. Requests and events are quite similar, just a use of different interaces. If you need more details please refere to the sample applications.

[Java Sample](https://github.com/jkratz55/spring-mediatr-java-sample)

[Kotlin Sample](https://github.com/jkratz55/spring-mediatr-kotlin-sample)

#### Command & CommandHandler

```java
public class CreateUserCommand implements Command {

	@NotBlank
	private final String userName;

	@NotBlank
	private final String email;

	@NotBlank
	private final String password;

	public CreateUserCommand(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
```

```java
@Component
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {

	private final UserRepository userRepository;

	@Autowired
	private Mediator mediator;

	public CreateUserCommandHandler(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void handle(@NotNull CreateUserCommand createUserCommand) {
		User user = new User(createUserCommand.getUserName(),
				createUserCommand.getEmail(),
				createUserCommand.getPassword());
		this.userRepository.save(user);
		this.mediator.emit(new UserCreatedEvent(user.getId()));
	}
}
```

### Dispatching and Emiting with the Mediator

Anywhere you want to dispatch or emit events you will need to inject an the Mediator bean. With the Mediator messages (commands, requests, or events) can be dispatched/emitted to the proper handler(s). The follow snippet below shows the Mediator being injected into a RESTful controller and then being used to dispatch messages.

```java
@RestController
public class SampleController {

	private final Mediator mediator;

	@Autowired
	public SampleController(Mediator mediator) {
		this.mediator = mediator;
	}

	@PostMapping("/command")
	public ResponseEntity<?> runCommand(@RequestBody CreateUserCommand command) {
		this.mediator.dispatch(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/request")
	public ResponseEntity<?> runRequest(@RequestBody CreateUserRequest request) {
		UUID id = this.mediator.dispatch(request);
		UriComponents uri = UriComponentsBuilder.fromPath("/user/{id}").buildAndExpand(id);
		return ResponseEntity.created(uri.toUri()).build();
	}

	@PostMapping("/requestAsync")
	public CompletableFuture<ResponseEntity> runRequestAsync(@RequestBody SlowCreateUserRequest request) {
		return this.mediator.dispatchAsync(request)
				.thenApply(id -> {
					UriComponents uri = UriComponentsBuilder.fromPath("/user/{id}").buildAndExpand(id);
					return ResponseEntity.created(uri.toUri()).build();
				});
	}
}
```


## Troubleshooting

### No Handler Exceptions

NoRequestHandlerException, NoCommandHandlerException, and NoEventHandlersException all have a common root; there was not a handler regiserted to handle that particular type of message. The two most likely causes are:

1. The Handler class is not being managed by Spring, IE forgot to add @Component to the class.
2. There is no class to handle that type of message

### Duplicate Handler Registration Exceptions

Both Commands and Request message types must map to a single handler. If a particular command or request is mapped to more than a single CommandHandler/RequestHandler the application will fail to start with an exception. Solution: Don't map a command or request to multiple handlers.



